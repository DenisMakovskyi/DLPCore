package ua.com.wl.dlp.domain.interactors.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

import ua.com.wl.dlp.data.api.AuthApi
import ua.com.wl.dlp.data.api.errors.ErrorsMapper
import ua.com.wl.dlp.data.api.requests.auth.*
import ua.com.wl.dlp.data.api.responses.PagedResponse
import ua.com.wl.dlp.data.api.responses.auth.TokenResponse
import ua.com.wl.dlp.data.api.responses.auth.AuthenticationResponse
import ua.com.wl.dlp.data.api.responses.auth.SignResponse
import ua.com.wl.dlp.data.api.responses.models.auth.CardsStatus
import ua.com.wl.dlp.data.api.responses.models.auth.City
import ua.com.wl.dlp.data.events.factory.CoreBusEventsFactory
import ua.com.wl.dlp.data.events.session.SessionBusEvent
import ua.com.wl.dlp.data.prefereces.ConsumerPreferences
import ua.com.wl.dlp.data.prefereces.CorePreferences
import ua.com.wl.dlp.domain.Result
import ua.com.wl.dlp.domain.UseCase
import ua.com.wl.dlp.domain.exeptions.ApiException
import ua.com.wl.dlp.domain.exeptions.auth.AuthException
import ua.com.wl.dlp.domain.interactors.AuthInteractor

/**
 * @author Denis Makovskyi
 */

class AuthInteractorImpl(
    private val api: AuthApi,
    errorsMapper: ErrorsMapper,
    private val corePreferences: CorePreferences,
    private val consumerPreferences: ConsumerPreferences) : AuthInteractor, UseCase(errorsMapper) {

    override suspend fun verification(): Result<TokenResponse> =
        callApi(
            call = { api.verification(TokenRequest(corePreferences.authPrefs.authToken)) },
            errorClass = AuthException::class.java
        ).flatMap { dataResponse ->
            dataResponse.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }.sOnSuccess { tokenResponse ->
            withContext(Dispatchers.IO) {
                corePreferences.authPrefs = corePreferences.authPrefs.copy(authToken = tokenResponse.token)
            }
        }

    override suspend fun refreshToken(): Result<TokenResponse> =
        callApi(
            call = { api.refreshToken(TokenRequest(corePreferences.authPrefs.refreshToken)) },
            errorClass = AuthException::class.java
        ).flatMap { dataResponse ->
            dataResponse.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }.sOnSuccess { tokenResponse ->
            withContext(Dispatchers.IO) {
                corePreferences.authPrefs = corePreferences.authPrefs.copy(authToken = tokenResponse.token)
            }
        }

    override suspend fun authentication(phone: String, sendSms: Boolean): Result<AuthenticationResponse> =
        callApi(
            call = { api.authentication(AuthenticationRequest(sendSms, phone)) },
            errorClass = AuthException::class.java
        ).flatMap { dataResponse ->
            dataResponse.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }

    override suspend fun signIn(phone: String, password: String): Result<SignResponse> =
        callApi(
            call = { api.signIn(SignInRequest(phone, password)) },
            errorClass = AuthException::class.java
        ).flatMap { dataResponse ->
            dataResponse.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }.sOnSuccess { tokenResponse ->
            withContext(Dispatchers.IO) {
                corePreferences.authPrefs = corePreferences.authPrefs.copy(
                    authToken = tokenResponse.token,
                    refreshToken = tokenResponse.refreshToken)
            }
        }

    override suspend fun cardsStatus(phone: String, password: String): Result<CardsStatus> =
        callApi(
            call = { api.cardsStatus(CardsStatusRequest(phone, password)) },
            errorClass = AuthException::class.java
        ).flatMap { dataResponse ->
            dataResponse.ifPresentOrDefault(
                { Result.Success(it.payload.cardsStatus) },
                { Result.Failure(ApiException()) })
        }

    override suspend fun signUp(city: Int, phone: String, password: String, barcode: String?): Result<SignResponse> =
        callApi(
            call = { api.signUp(SignUpRequest(city, phone, password, barcode)) },
            errorClass = AuthException::class.java
        ).flatMap { dataResponse ->
            dataResponse.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }.sOnSuccess { tokenResponse ->
            withContext(Dispatchers.IO) {
                corePreferences.authPrefs = corePreferences.authPrefs.copy(
                    authToken = tokenResponse.token,
                    refreshToken = tokenResponse.refreshToken)
            }
        }

    override suspend fun signOut(): Result<Boolean> =
        callApi(
            call = { api.signOut() },
            errorClass = AuthException::class.java
        ).map { baseResponse ->
            baseResponse.getUnsafe()?.isSuccessfully() ?: false
        }.sOnEach {
            withContext(Dispatchers.IO) {
                corePreferences.removeAuthPrefs()
                consumerPreferences.removeProfilePrefs()
            }
            CoreBusEventsFactory.session(SessionBusEvent.FallbackType.SIGNED_OUT)
        }

    override suspend fun requestSmsCode(phone: String): Result<Boolean> =
        callApi(
            call = { api.requestSmsCode(SmsCodeRequest(phone)) },
            errorClass = AuthException::class.java
        ).map { baseResponse ->
            baseResponse.getUnsafe()?.isSuccessfully() ?: false
        }

    override suspend fun cities(): Result<PagedResponse<City>> =
        callApi(call = { api.cities() }).flatMap { dataResponse ->
            dataResponse.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }
}