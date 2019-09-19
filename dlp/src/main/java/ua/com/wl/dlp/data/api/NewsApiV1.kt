package ua.com.wl.dlp.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

import ua.com.wl.dlp.data.api.responses.PagedResponse
import ua.com.wl.dlp.data.api.responses.news.BaseArticleResponse
import ua.com.wl.dlp.data.api.responses.news.ArticleResponse

/**
 * @author Denis Makovskyi
 */

@Deprecated(message = "Needs further refactoring and merging with api/mobile/v2")
interface NewsApiV1 {

    @GET("api/mobile/v1/consumer/news/feed/")
    suspend fun getCityNewsFeed(
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null
    ): Response<PagedResponse<BaseArticleResponse>>

    @GET("api/mobile/v1/consumer/news/feed/shop/{shop_id}/")
    suspend fun getShopNewsFeed(
        @Path("shop_id") shopId: Int,
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null
    ): Response<PagedResponse<BaseArticleResponse>>

    @GET("api/mobile/v1/consumer/news/{item_id}/")
    suspend fun getNewsItem(@Path("item_id")id: Int): Response<ArticleResponse>
}