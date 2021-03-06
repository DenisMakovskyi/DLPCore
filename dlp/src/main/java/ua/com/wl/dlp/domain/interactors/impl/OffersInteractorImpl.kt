package ua.com.wl.dlp.domain.interactors.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import android.app.Application

import ua.com.wl.dlp.core.Constants
import ua.com.wl.dlp.data.db.datasources.ShopsDataSource
import ua.com.wl.dlp.data.api.OffersApiV1
import ua.com.wl.dlp.data.api.errors.ErrorsMapper
import ua.com.wl.dlp.data.api.responses.shop.offer.OfferResponse
import ua.com.wl.dlp.data.api.responses.consumer.history.transactions.BalanceChangeResponse
import ua.com.wl.dlp.data.prefereces.ConsumerPreferences
import ua.com.wl.dlp.data.events.factory.CoreBusEventsFactory
import ua.com.wl.dlp.domain.Result
import ua.com.wl.dlp.domain.UseCase
import ua.com.wl.dlp.domain.exeptions.api.ApiException
import ua.com.wl.dlp.domain.interactors.OffersInteractor
import ua.com.wl.dlp.utils.mapIsSuccessfully
import ua.com.wl.dlp.utils.sendBroadcastMessage
import ua.com.wl.dlp.utils.processBalanceChanges
import ua.com.wl.dlp.utils.updatePreOrdersCounter

/**
 * @author Denis Makovskyi
 */

class OffersInteractorImpl(
    errorsMapper: ErrorsMapper,
    private val app: Application,
    private val apiV1: OffersApiV1,
    private val shopsDataSource: ShopsDataSource,
    private val consumerPreferences: ConsumerPreferences
) : UseCase(errorsMapper), OffersInteractor {

    override suspend fun addOfferToFavourites(offerId: Int): Result<Boolean> {
        return callApi(call = { apiV1.addOfferToFavourite(offerId) })
            .mapIsSuccessfully()
            .sOnSuccess { isSuccess ->
                if (isSuccess) {
                    withContext(Dispatchers.Main.immediate) {
                        CoreBusEventsFactory.offerFavouriteStatus(
                            offerId = offerId,
                            isFavourite = true)
                    }
                }
            }
    }

    override suspend fun removeOfferFromFavourites(offerId: Int): Result<Boolean> {
        return callApi(call = { apiV1.removeOfferFromFavourite(offerId) })
            .mapIsSuccessfully()
            .sOnSuccess { isSuccess ->
                if (isSuccess) {
                    withContext(Dispatchers.Main.immediate) {
                        CoreBusEventsFactory.offerFavouriteStatus(
                            offerId = offerId,
                            isFavourite = false)
                    }
                }
            }
    }

    override suspend fun getOffer(offerId: Int, shopId: Int?): Result<OfferResponse> {
        return callApi(call = { apiV1.getOffer(offerId) })
            .sFlatMap { responseOpt ->
                responseOpt.sIfPresentOrDefault(
                    { offer ->
                        if (shopId != null) {
                            updatePreOrdersCounter(
                                shopId, offer, shopsDataSource, Dispatchers.IO)
                        }
                        Result.Success(offer)
                    },
                    { Result.Failure(ApiException()) })
            }
    }

    override suspend fun collectBonusesPerOfferView(offerId: Int): Result<BalanceChangeResponse> {
        return callApi(call = { apiV1.collectBonusesPerView(offerId) })
            .flatMap { responseOpt ->
                responseOpt.ifPresentOrDefault(
                    {
                        if (it.change != null) {
                            Result.Success(it)
                        } else {
                            Result.Failure(ApiException())
                        }
                    },
                    { Result.Failure(ApiException()) })
            }.sOnSuccess { changeResponse ->
                val change = changeResponse.change
                if (change != null) {
                    val changes = processBalanceChanges(Dispatchers.IO, changeResponse.change, consumerPreferences)
                    if (changes.isNotEmpty()) {
                        withContext(Dispatchers.Main.immediate) {
                            CoreBusEventsFactory.profileChanges(changes)
                            sendBroadcastMessage(app, Constants.RECEIVER_ACTION_SOUND_BONUSES)
                        }
                    }
                }
            }
    }
}