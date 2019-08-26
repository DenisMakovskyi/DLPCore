package ua.com.wl.dlp.data.api

import retrofit2.Response
import retrofit2.http.*
import ua.com.wl.dlp.data.api.responses.BaseResponse

import ua.com.wl.dlp.data.api.responses.PagedResponse
import ua.com.wl.dlp.data.api.responses.shop.news.ShopNewsItemResponse
import ua.com.wl.dlp.data.api.responses.shop.CityShopsResponse
import ua.com.wl.dlp.data.api.responses.shop.offer.BaseShopOfferResponse
import ua.com.wl.dlp.data.api.responses.shop.offer.ShopOfferResponse

/**
 * @author Denis Makovskyi
 */

@Deprecated(message = "Needs further refactoring for api/v3")
interface ShopApiV1 {

    @GET("api/v1/consumer/city-shops/")
    suspend fun getCityShops(
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null

    ): Response<PagedResponse<CityShopsResponse>>

    @GET("api/v1/consumer/news/feed/shop/{shop_id}/")
    suspend fun getShopNewsFeed(
        @Path("shop_id") shopId: Int,
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null

    ): Response<PagedResponse<ShopNewsItemResponse>>

    @GET("api/v1/consumer/shops/{shop_id}/promo-offers/")
    suspend fun getShopPromoOffers(
        @Path("shop_id") shopId: Int,
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null

    ): Response<PagedResponse<BaseShopOfferResponse>>

    @GET("api/v1/consumer/favorite-offers/shop/{shop_id}/")
    suspend fun getShopFavouriteOffers(
        @Path("shop_id") shopId: Int,
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null

    ): Response<PagedResponse<BaseShopOfferResponse>>

    @POST("api/v1/consumer/favourite-offers/{offer_id}")
    suspend fun addShopOfferToFavourite(@Path("offer_id") offerId: Int): Response<BaseResponse>

    @DELETE("api/v1/consumer/favourite-offers/{offer_id}")
    suspend fun removeShopOfferFromFavourite(@Path("offer_id") offerId: Int) : Response<BaseResponse>

    @GET("api/v1/consumer/offers/{offer_id}/")
    suspend fun getShopOffer(@Path("offer_id") offerId: Int): Response<ShopOfferResponse>
}