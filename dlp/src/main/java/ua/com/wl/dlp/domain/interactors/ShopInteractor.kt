package ua.com.wl.dlp.domain.interactors

import ua.com.wl.dlp.data.api.responses.PagedResponse
import ua.com.wl.dlp.data.api.responses.models.shop.ShopNewsItem
import ua.com.wl.dlp.data.api.responses.models.shop.ShopPromoOffer
import ua.com.wl.dlp.data.api.responses.shop.CityShopsResponse
import ua.com.wl.dlp.domain.Result

/**
 * @author Denis Makovskyi
 */

interface ShopInteractor {

    suspend fun getCityShops(
        page: Int? = null, count: Int? = null): Result<PagedResponse<CityShopsResponse>>

    suspend fun getShopNewsFeed(
        shopId: Int, page: Int? = null, count: Int? = null): Result<PagedResponse<ShopNewsItem>>

    suspend fun getShopPromoOffers(
        shopId: Int, page: Int? = null, count: Int? = null): Result<PagedResponse<ShopPromoOffer>>
}