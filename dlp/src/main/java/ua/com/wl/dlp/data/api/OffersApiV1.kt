package ua.com.wl.dlp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.DELETE

import ua.com.wl.dlp.data.api.responses.BaseResponse
import ua.com.wl.dlp.data.api.responses.shop.offer.OfferResponse
import ua.com.wl.dlp.data.api.responses.consumer.history.BalanceChangeResponse

/**
 * @author Denis Makovskyi
 */

@Deprecated(message = "Needs further refactoring for api/mobile/v2")
interface OffersApiV1 {

    @POST("api/mobile/v1/consumer/favorite-offers/{offer_id}/")
    suspend fun addOfferToFavourite(@Path("offer_id") offerId: Int): Response<BaseResponse>

    @DELETE("api/mobile/v1/consumer/favorite-offers/{offer_id}/")
    suspend fun removeOfferFromFavourite(@Path("offer_id") offerId: Int): Response<BaseResponse>

    @GET("api/mobile/v1/consumer/offers/{offer_id}/")
    suspend fun getOffer(@Path("offer_id") offerId: Int): Response<OfferResponse>

    @POST("api/mobile/v1/consumer/offers/{offer_id}/view/")
    suspend fun collectBonusesPerView(@Path("offer_id") offerId: Int): Response<BalanceChangeResponse>
}