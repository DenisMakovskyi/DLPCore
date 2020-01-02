package ua.com.wl.dlp.domain.workers

import java.util.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import android.content.Context

import androidx.work.*

import org.koin.core.inject

import ua.com.wl.dlp.core.DLPCoreComponent
import ua.com.wl.dlp.data.api.responses.shop.offer.BaseOfferResponse
import ua.com.wl.dlp.data.db.entities.shops.OfferEntity
import ua.com.wl.dlp.data.db.entities.shops.ShopEntity
import ua.com.wl.dlp.domain.interactors.ShopInteractor
import ua.com.wl.dlp.utils.Failure
import ua.com.wl.dlp.utils.Success

/**
 * @author Denis Makovskyi
 */

class ShopOffersSyncWork(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), DLPCoreComponent {

    companion object {

        val TAG = ShopOffersSyncWork::class.java.name

        const val INPUT_KEY_SHOP_ID = "shop_id"

        const val ERROR_KEY_WHEN_READ_SHOP = "error_when_read_shop"
        const val ERROR_KEY_WHEN_READ_ORDERS = "error_when_read_orders"
        const val ERROR_KEY_WHEN_LOAD_OFFERS = "error_when_load_offers"
        const val ERROR_KEY_WHEN_WRITE_IN_DB = "error_when_write_in_db"

        fun schedule(context: Context, shopId: Int): UUID {
            val request = OneTimeWorkRequestBuilder<ShopOffersSyncWork>()
                .apply {
                    addTag(TAG)
                    setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build())
                    setInputData(
                        Data.Builder()
                            .putInt(INPUT_KEY_SHOP_ID, shopId)
                            .build())
                }.build()
            WorkManager.getInstance(context)
                .beginUniqueWork(TAG, ExistingWorkPolicy.REPLACE, request)
                .enqueue()
            return request.id
        }

        fun containsError(key: String, data: Data): Boolean =
            data.keyValueMap.containsKey(key) && data.getBoolean(key, false)
    }

    private val shopId = inputData.getInt(INPUT_KEY_SHOP_ID, 0)
    private val shopInteractor: ShopInteractor by inject()

    private val outputs: Data.Builder = Data.Builder()

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
            launch()
        }
        val data = outputs.build()
        return if (data.keyValueMap.isEmpty()) {
            Result.success()
        } else {
            Result.failure(data)
        }
    }

    private suspend fun launch() {
        shopInteractor.getPersistedShop(shopId)
            .sOnSuccess { shopEntityOpt ->
                shopEntityOpt.sIfPresentOrElse(
                    { getPersistedOffers(it) },
                    { outputs.putBoolean(ERROR_KEY_WHEN_READ_SHOP, true) })
            }.onFailure {
                outputs.putBoolean(ERROR_KEY_WHEN_READ_SHOP, true)
            }
    }

    private suspend fun getPersistedOffers(shop: ShopEntity) {
        shopInteractor.getPersistedOffers(shop.id)
            .sOnSuccess { offers ->
                loadOffers(shop.id, offers)
            }.onFailure {
                outputs.putBoolean(ERROR_KEY_WHEN_READ_ORDERS, true)
            }
    }

    private suspend fun loadOffers(shopId: Int, preOrders: List<OfferEntity>) {
        for (preOrder in preOrders) {
            when(val offerResult = shopInteractor.getOffer(preOrder.id)) {
                is Success -> updatePersistedOffer(shopId, offerResult.data)
                is Failure -> outputs.putBoolean(ERROR_KEY_WHEN_LOAD_OFFERS, true)
            }
        }
        shopInteractor.populatePersistedOffersPrice(shopId)
    }

    private suspend fun updatePersistedOffer(shopId: Int, offer: BaseOfferResponse) {
        shopInteractor.updatePersistedOffer(shopId, offer)
            .onFailure {
                outputs.putBoolean(ERROR_KEY_WHEN_WRITE_IN_DB, true)
            }
    }
}