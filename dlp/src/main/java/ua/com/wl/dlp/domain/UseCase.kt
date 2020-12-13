package ua.com.wl.dlp.domain

import retrofit2.HttpException
import retrofit2.Response
import ua.com.wl.archetype.utils.Optional
import ua.com.wl.dlp.data.api.errors.ErrorsMapper
import ua.com.wl.dlp.domain.exeptions.api.ApiException
import ua.com.wl.dlp.domain.exeptions.db.DatabaseException

open class UseCase(private val errorsMapper: ErrorsMapper) {

    protected suspend fun <T : Any> callApi(
        call: suspend () -> Response<T>,
        errorMapper: ((type: String?, cause: Throwable?) -> ApiException)? = null
    ): Result<Optional<T>> {
        return try {
            val response = call.invoke()
            if (response.isSuccessful) {
                Result.Success(Optional.ofNullable(response.body()))
            } else {
                val throwable = if (errorMapper != null) {
                    errorsMapper.createExceptionFromResponse(response, errorMapper)
                } else {
                    ApiException(cause = HttpException(response))
                }
                Result.Failure(throwable)
            }

        } catch (e: Exception) {
            Result.Failure(ApiException(cause = e))
        }
    }

    protected suspend fun <T : Any> callQuery(call: suspend () -> T): Result<T> {
        return try {
            Result.Success(call.invoke())

        } catch (e: Exception) {
            Result.Failure(DatabaseException(cause = e))
        }
    }
}
