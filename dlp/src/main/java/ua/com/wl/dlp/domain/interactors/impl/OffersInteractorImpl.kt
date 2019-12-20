package ua.com.wl.dlp.domain.interactors.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import ua.com.wl.dlp.data.api.OffersApiV1
import ua.com.wl.dlp.data.api.errors.ErrorsMapper
import ua.com.wl.dlp.data.api.responses.shop.offer.OfferResponse
import ua.com.wl.dlp.data.events.factory.CoreBusEventsFactory
import ua.com.wl.dlp.domain.Result
import ua.com.wl.dlp.domain.UseCase
import ua.com.wl.dlp.domain.exeptions.api.ApiException
import ua.com.wl.dlp.domain.interactors.OffersInteractor

/**
 * @author Denis Makovskyi
 */

class OffersInteractorImpl constructor(
    errorsMapper: ErrorsMapper,
    private val apiV1: OffersApiV1
) : UseCase(errorsMapper), OffersInteractor {

    override suspend fun addOfferToFavourites(offerId: Int): Result<Boolean> =
        callApi(call = { apiV1.addOfferToFavourite(offerId) })
            .map { responseOpt ->
                responseOpt.ifPresentOrDefault(
                    { it.isSuccessfully() },
                    { false })
            }.sOnSuccess { isSuccess ->
                if (isSuccess) {
                    withContext(Dispatchers.Main.immediate) {
                        CoreBusEventsFactory.offerFavouriteStatus(
                            offerId = offerId,
                            isFavourite = true)
                    }
                }
            }

    override suspend fun removeOfferFromFavourites(offerId: Int): Result<Boolean> =
        callApi(call = { apiV1.removeOfferFromFavourite(offerId) })
            .map { responseOpt ->
                responseOpt.ifPresentOrDefault(
                    { it.isSuccessfully() },
                    { false })
            }.sOnSuccess { isSuccess ->
                if (isSuccess) {
                    withContext(Dispatchers.Main.immediate) {
                        CoreBusEventsFactory.offerFavouriteStatus(
                            offerId = offerId,
                            isFavourite = false)
                    }
                }
            }

    override suspend fun getOffer(offerId: Int): Result<OfferResponse> =
        callApi(call = { apiV1.getOffer(offerId) })
            .flatMap { response ->
                response.ifPresentOrDefault(
                    { Result.Success(it) },
                    { Result.Failure(ApiException()) })
            }
}