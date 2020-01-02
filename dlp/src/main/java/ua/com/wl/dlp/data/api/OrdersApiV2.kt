package ua.com.wl.dlp.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

import ua.com.wl.dlp.data.api.requests.orders.order.GeneralPreOrderCreationRequest
import ua.com.wl.dlp.data.api.responses.CollectionResponse
import ua.com.wl.dlp.data.api.responses.models.orders.order.GeneralPreOrderItem

/**
 * @author Denis Makovskyi
 */

interface OrdersApiV2 {

    @POST("api/mobile/v2/basket/")
    suspend fun createGeneralPreOrder(@Body request: GeneralPreOrderCreationRequest): Response<CollectionResponse<GeneralPreOrderItem>>
}