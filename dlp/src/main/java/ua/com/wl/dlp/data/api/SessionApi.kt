package ua.com.wl.dlp.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import ua.com.wl.dlp.core.Constants
import ua.com.wl.dlp.data.api.requests.auth.TokenRequest
import ua.com.wl.dlp.data.api.responses.DataResponse
import ua.com.wl.dlp.data.api.responses.auth.TokenResponse

/**
 * @author Denis Makovskyi
 */

interface SessionApi {

//    @Headers("${Constants.HEADER_UNAUTHORIZED}: ${Constants.VALUE_PERMIT}")
    @POST("api/mobile/v2/consumer/auth/token/refresh/")
    suspend fun refreshToken(
        @Body request: TokenRequest,
        @Header(Constants.HEADER_APP_ID) appId: String
    ): Response<DataResponse<TokenResponse>>
}