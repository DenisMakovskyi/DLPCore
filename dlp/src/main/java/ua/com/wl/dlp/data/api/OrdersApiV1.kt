package ua.com.wl.dlp.data.api

import retrofit2.Response
import retrofit2.http.*

import ua.com.wl.dlp.data.api.requests.orders.order.PreOrderRequest
import ua.com.wl.dlp.data.api.requests.orders.order.RateOrderRequest
import ua.com.wl.dlp.data.api.requests.orders.table.TableReservationRequest
import ua.com.wl.dlp.data.api.responses.BaseResponse
import ua.com.wl.dlp.data.api.responses.PagedResponse
import ua.com.wl.dlp.data.api.responses.orders.order.*
import ua.com.wl.dlp.data.api.responses.orders.order.pre_order.BasePreOrderResponse
import ua.com.wl.dlp.data.api.responses.orders.order.pre_order.PreOrderCreationResponse
import ua.com.wl.dlp.data.api.responses.orders.order.pre_order.PreOrderResponse
import ua.com.wl.dlp.data.api.responses.orders.order.rate.BaseOrderRateResponse
import ua.com.wl.dlp.data.api.responses.orders.order.rate.OrderRateResponse
import ua.com.wl.dlp.data.api.responses.orders.table.TableReservationDetailedResponse
import ua.com.wl.dlp.data.api.responses.orders.table.TableReservationItemResponse

/**
 * @author Denis Makovskyi
 */

@Deprecated(message = "Needs further refactoring for api/mobile/v2")
interface OrdersApiV1 {

    @GET("api/mobile/v1/consumer/orders/")
    suspend fun getOrders(
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null
    ): Response<PagedResponse<OrderSimpleResponse>>

    @GET("api/mobile/v1/consumer/orders/{order_id}/")
    suspend fun getOrder(@Path("order_id") orderId: Int): Response<OrderResponse>

    @PATCH("api/mobile/v1/consumer/order-rates/{order_id}/")
    suspend fun rateOrder(
        @Path("order_id") orderId: Int,
        @Body request: RateOrderRequest
    ): Response<BaseOrderRateResponse>

    @GET("api/mobile/v1/consumer/order-rates/{order_id}/")
    suspend fun getOrderRate(@Path("order_id") orderId: Int): Response<OrderRateResponse>

    @GET("api/mobile/v1/consumer/last-order-rate/")
    suspend fun getLastOrderRate(): Response<OrderRateResponse>

    @POST("api/mobile/v1/consumer/pre-orders/")
    suspend fun createPreOrder(@Body request: PreOrderRequest): Response<PreOrderCreationResponse>

    @GET("api/mobile/v1/consumer/pre-orders/")
    suspend fun getPreOrders(
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null
    ): Response<PagedResponse<BasePreOrderResponse>>

    @GET("api/mobile/v1/consumer/pre-orders/{pre_order_id}")
    suspend fun getPreOrder(@Path("pre_order_id") preOrderId: Int): Response<PreOrderResponse>

    @POST("api/mobile/v1/consumer/table-reservations/")
    suspend fun createTableReservation(@Body request: TableReservationRequest): Response<TableReservationItemResponse>

    @GET("api/mobile/v1/consumer/table-reservations/")
    suspend fun getTablesReservations(
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null
    ): Response<PagedResponse<TableReservationItemResponse>>

    @GET("api/mobile/v1/consumer/table-reservation/{reservation_id}/")
    suspend fun getTableReservation(@Path("reservation_id") reservationId: Int): Response<TableReservationDetailedResponse>

    @PATCH("api/mobile/v1/consumer/table-reservation/{reservation_id}/reject/")
    suspend fun cancelTableReservation(@Path("reservation_id") reservationId: Int): Response<BaseResponse>
}