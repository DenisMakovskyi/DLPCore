package ua.com.wl.dlp.data.api

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

import ua.com.wl.dlp.data.api.responses.BaseResponse
import ua.com.wl.dlp.data.api.responses.shop.offer.OfferResponse

/**
 * @author Denis Makovskyi
 */

interface OffersApi {

    @POST("api/mobile/v1/consumer/favorite-offers/{offer_id}/")
    suspend fun addOfferToFavourite(@Path("offer_id") offerId: Int): Response<BaseResponse>

    @DELETE("api/mobile/v1/consumer/favorite-offers/{offer_id}/")
    suspend fun removeOfferFromFavourite(@Path("offer_id") offerId: Int): Response<BaseResponse>

    @GET("api/mobile/v1/consumer/offers/{offer_id}/")
    suspend fun getOffer(@Path("offer_id") offerId: Int): Response<OfferResponse>
}