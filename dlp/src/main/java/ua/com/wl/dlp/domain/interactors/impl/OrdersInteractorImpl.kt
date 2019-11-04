package ua.com.wl.dlp.domain.interactors.impl

import ua.com.wl.dlp.data.api.OrdersApiV1
import ua.com.wl.dlp.data.api.errors.ErrorsMapper
import ua.com.wl.dlp.data.api.requests.orders.order.PreOrderCreationRequest
import ua.com.wl.dlp.data.api.requests.orders.order.RateOrderRequest
import ua.com.wl.dlp.data.api.requests.orders.table.TableReservationRequest
import ua.com.wl.dlp.data.api.responses.PagedResponse
import ua.com.wl.dlp.data.api.responses.orders.order.*
import ua.com.wl.dlp.data.api.responses.orders.table.TableReservationResponse
import ua.com.wl.dlp.domain.Result
import ua.com.wl.dlp.domain.UseCase
import ua.com.wl.dlp.domain.exeptions.api.ApiException
import ua.com.wl.dlp.domain.interactors.OrdersInteractor

/**
 * @author Denis Makovskyi
 */

class OrdersInteractorImpl(
    errorsMapper: ErrorsMapper,
    private val apiV1: OrdersApiV1
): UseCase(errorsMapper), OrdersInteractor {

    override suspend fun getOrders(page: Int?, count: Int?): Result<PagedResponse<OrderSimpleResponse>> =
        callApi(call = { apiV1.getOrders(page, count) })
            .flatMap { response ->
                response.ifPresentOrDefault(
                    { Result.Success(it) },
                    { Result.Failure(ApiException()) })
            }

    override suspend fun getOrder(orderId: Int): Result<OrderResponse> =
        callApi(call = { apiV1.getOrder(orderId) })
            .flatMap { response ->
                response.ifPresentOrDefault(
                    { Result.Success(it) },
                    { Result.Failure(ApiException()) })
            }

    override suspend fun rateOrder(
        orderId: Int,
        value: Int,
        comment: String
    ): Result<BaseOrderRateResponse> =
        callApi(call = { apiV1.rateOrder(orderId, RateOrderRequest(value, comment)) })
            .flatMap { response ->
                response.ifPresentOrDefault(
                    { Result.Success(it) },
                    { Result.Failure(ApiException()) })
            }

    override suspend fun getOrderRate(orderId: Int): Result<OrderRateResponse> =
        callApi(call = { apiV1.getOrderRate(orderId) })
            .flatMap { response ->
                response.ifPresentOrDefault(
                    { Result.Success(it) },
                    { Result.Failure(ApiException()) })
            }

    override suspend fun getLastOrderRate(): Result<OrderRateResponse> =
        callApi(call = { apiV1.getLastOrderRate() })
            .flatMap { response ->
                response.ifPresentOrDefault(
                    { Result.Success(it) },
                    { Result.Failure(ApiException()) })
            }

    override suspend fun createPreOrder(request: PreOrderCreationRequest): Result<PreOrderCreationResponse> =
        callApi(call = { apiV1.createPreOrder(request) }).flatMap { response ->
            response.ifPresentOrDefault(
                { Result.Success(it) },
                { Result.Failure(ApiException()) })
        }

    override suspend fun getPreOrders(
        page: Int?,
        count: Int?
    ): Result<PagedResponse<BasePreOrderResponse>> =
        callApi(call = { apiV1.getPreOrders(page, count) }).flatMap { response ->
            response.ifPresentOrDefault(
                { Result.Success(it) },
                { Result.Failure(ApiException()) })
        }

    override suspend fun getPreOrder(preOrderId: Int): Result<PreOrderResponse> =
        callApi(call = { apiV1.getPreOrder(preOrderId) }).flatMap { response ->
            response.ifPresentOrDefault(
                { Result.Success(it) },
                { Result.Failure(ApiException()) })
        }

    override suspend fun createTableReservation(request: TableReservationRequest): Result<TableReservationResponse> =
        callApi(call = { apiV1.createTableReservation(request) }).flatMap { response ->
            response.ifPresentOrDefault(
                { Result.Success(it) },
                { Result.Failure(ApiException()) })
        }

    override suspend fun getTablesReservations(
        page: Int?,
        count: Int?
    ): Result<PagedResponse<TableReservationResponse>> =
        callApi(call = { apiV1.getTablesReservations(page, count) }).flatMap { response ->
            response.ifPresentOrDefault(
                { Result.Success(it) },
                { Result.Failure(ApiException()) })
        }

    override suspend fun getTableReservation(reservationId: Int): Result<TableReservationResponse> =
        callApi(call = { apiV1.getTableReservation(reservationId) }).flatMap { response ->
            response.ifPresentOrDefault(
                { Result.Success(it) },
                { Result.Failure(ApiException()) })
        }

    override suspend fun cancelTableReservation(reservationId: Int): Result<Boolean> =
        callApi(call = { apiV1.cancelTableReservation(reservationId) }).map { response ->
            response.ifPresentOrDefault(
                { it.isSuccessfully() },
                { false })
        }
}