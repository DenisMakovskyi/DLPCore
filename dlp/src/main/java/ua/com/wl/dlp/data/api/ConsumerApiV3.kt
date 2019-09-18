package ua.com.wl.dlp.data.api

import retrofit2.Response
import retrofit2.http.*

import ua.com.wl.dlp.data.api.requests.consumer.profile.ProfileRequest
import ua.com.wl.dlp.data.api.requests.consumer.referral.ReferralRequest
import ua.com.wl.dlp.data.api.responses.DataResponse
import ua.com.wl.dlp.data.api.responses.PagedResponse
import ua.com.wl.dlp.data.api.responses.consumer.history.TransactionResponse
import ua.com.wl.dlp.data.api.responses.consumer.profile.ProfileResponse
import ua.com.wl.dlp.data.api.responses.consumer.referral.QrCodeResponse
import ua.com.wl.dlp.data.api.responses.consumer.referral.ReferralResponse

/**
 * @author Denis Makovskyi
 */

interface ConsumerApiV3 {

    @GET("api/v3/consumer/profile/")
    suspend fun getProfile(): Response<DataResponse<ProfileResponse>>

    @PATCH("api/v3/consumer/profile/")
    suspend fun updateProfile(@Body request: ProfileRequest): Response<DataResponse<ProfileResponse>>

    @GET("api/v3/consumer/qr-code/")
    suspend fun getQrCode(): Response<DataResponse<QrCodeResponse>>

    @POST("api/v3/consumer/refer-lead/")
    suspend fun useReferralCode(@Body request: ReferralRequest): Response<DataResponse<ReferralResponse>>

    @GET("api/v3/consumer/balance/history/")
    suspend fun loadTransactionsHistory(
        @Query("page") page: Int? = null,
        @Query("page_size") count: Int? = null
    ): Response<DataResponse<PagedResponse<TransactionResponse>>>
}