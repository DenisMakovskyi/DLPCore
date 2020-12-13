package ua.com.wl.dlp.domain.interactors.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.com.wl.dlp.data.api.AuthApiV1
import ua.com.wl.dlp.data.api.AuthApiV2
import ua.com.wl.dlp.data.api.errors.ErrorsMapper
import ua.com.wl.dlp.data.api.requests.auth.*
import ua.com.wl.dlp.data.api.responses.PagedResponse
import ua.com.wl.dlp.data.api.responses.auth.AuthenticationResponse
import ua.com.wl.dlp.data.api.responses.auth.SignResponse
import ua.com.wl.dlp.data.api.responses.auth.TokenResponse
import ua.com.wl.dlp.data.api.responses.models.auth.CardsStatus
import ua.com.wl.dlp.data.api.responses.models.auth.City
import ua.com.wl.dlp.data.events.factory.CoreBusEventsFactory
import ua.com.wl.dlp.data.events.session.SessionBusEvent
import ua.com.wl.dlp.data.prefereces.ConsumerPreferences
import ua.com.wl.dlp.data.prefereces.CorePreferences
import ua.com.wl.dlp.domain.Result
import ua.com.wl.dlp.domain.UseCase
import ua.com.wl.dlp.domain.exeptions.api.ApiException
import ua.com.wl.dlp.domain.exeptions.api.auth.AuthException
import ua.com.wl.dlp.domain.interactors.AuthInteractor

class AuthInteractorImpl(
    errorsMapper: ErrorsMapper,
    private val apiV1: AuthApiV1,
    private val apiV2: AuthApiV2,
    private val corePreferences: CorePreferences,
    private val consumerPreferences: ConsumerPreferences
) : UseCase(errorsMapper), AuthInteractor {

    override suspend fun verification(): Result<TokenResponse> {
        return callApi(
            call = { apiV2.verification(TokenRequest(corePreferences.authPrefs.authToken)) },
            errorMapper = { type, cause -> AuthException(type, cause) }
        )
            .flatMap { dataResponseOpt ->
            dataResponseOpt.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }.sOnSuccess { tokenResponse ->
            withContext(Dispatchers.IO) {
                corePreferences.authPrefs = corePreferences.authPrefs.copy(authToken = tokenResponse.token)
            }
        }
    }

    override suspend fun refreshToken(): Result<TokenResponse> {
        return callApi(
            call = { apiV2.refreshToken(TokenRequest(corePreferences.authPrefs.refreshToken)) },
            errorMapper = { type, cause -> AuthException(type, cause) }
        ).flatMap { dataResponseOpt ->
            dataResponseOpt.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }.sOnSuccess { tokenResponse ->
            withContext(Dispatchers.IO) {
                corePreferences.authPrefs = corePreferences.authPrefs.copy(authToken = tokenResponse.token)
            }
        }
    }

    override suspend fun authentication(request: AuthenticationRequest): Result<AuthenticationResponse> {
        return callApi(
            call = { apiV2.authentication(request) },
            errorMapper = { type, cause -> AuthException(type, cause) }
        ).flatMap { dataResponseOpt ->
            dataResponseOpt.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }
    }

    override suspend fun signIn(request: SignInRequest, appVersion: String?): Result<SignResponse> {
        return callApi(
            call = { apiV2.signIn(request) },
            errorMapper = { type, cause -> AuthException(type, cause) }
        ).flatMap { dataResponseOpt ->
            dataResponseOpt.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }.sOnSuccess { tokenResponse ->
            withContext(Dispatchers.IO) {
                corePreferences.authPrefs = corePreferences.authPrefs.copy(
                    authToken = tokenResponse.token,
                    refreshToken = tokenResponse.refreshToken)
                callApi(call = { apiV2.deviceInfo(DeviceInfoRequest(appVersion = appVersion)) })
            }
        }
    }

    override suspend fun cardsStatus(request: CardsStatusRequest): Result<CardsStatus> {
        return callApi(
            call = { apiV2.cardsStatus(request) },
            errorMapper = { type, cause -> AuthException(type, cause) }
        ).flatMap { dataResponseOpt ->
            dataResponseOpt.ifPresentOrDefault(
                { Result.Success(it.payload.cardsStatus) },
                { Result.Failure(ApiException()) })
        }
    }

    override suspend fun signUp(request: SignUpRequest, appVersion: String?): Result<SignResponse> {
        return callApi(
            call = { apiV2.signUp(request) },
            errorMapper = { type, cause -> AuthException(type, cause) }
        ).flatMap { dataResponseOpt ->
            dataResponseOpt.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }.sOnSuccess { tokenResponse ->
            withContext(Dispatchers.IO) {
                corePreferences.authPrefs = corePreferences.authPrefs.copy(
                    authToken = tokenResponse.token,
                    refreshToken = tokenResponse.refreshToken)
                callApi(call = { apiV2.deviceInfo(DeviceInfoRequest(appVersion = appVersion)) })
            }
        }
    }

    override suspend fun signOut(): Result<Boolean> {
        return callApi(
            call = { apiV2.signOut() },
            errorMapper = { type, cause -> AuthException(type, cause) }
        ).map { baseResponseOpt ->
            baseResponseOpt.getUnsafe()?.isSuccessfully() ?: false
        }.sOnEach {
            withContext(Dispatchers.IO) {
                corePreferences.removeAuthPrefs()
                consumerPreferences.removeRankPrefs()
                consumerPreferences.removeProfilePrefs()
            }
            CoreBusEventsFactory.sessionExpired(SessionBusEvent.FallbackType.SIGNED_OUT)
        }
    }

    override suspend fun requestSmsCode(request: SmsCodeRequest): Result<Boolean> {
        return callApi(
            call = { apiV2.requestSmsCode(request) },
            errorMapper = { type, cause -> AuthException(type, cause) }
        ).map { baseResponseOpt ->
            baseResponseOpt.getUnsafe()?.isSuccessfully() ?: false
        }
    }

    override suspend fun restorePassword(request: SmsCodeRequest): Result<Boolean> {
        return callApi(call = { apiV1.restorePassword(request) }).map { baseResponseOpt ->
            baseResponseOpt.getUnsafe()?.isSuccessfully() ?: false
        }
    }

    override suspend fun cities(): Result<PagedResponse<City>> {
        return callApi(call = { apiV2.cities() }).flatMap { dataResponseOpt ->
            dataResponseOpt.ifPresentOrDefault(
                { Result.Success(it.payload) },
                { Result.Failure(ApiException()) })
        }
    }
}