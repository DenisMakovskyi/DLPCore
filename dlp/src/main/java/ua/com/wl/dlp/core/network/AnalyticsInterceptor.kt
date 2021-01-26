package ua.com.wl.dlp.core.network

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import okhttp3.Interceptor
import okhttp3.Response

class AnalyticsInterceptor(
    context: Context
) : Interceptor {

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.isSuccessfulOr3xx) {
            return response
        }

        val url = request.url.encodedPath
        val headers = request.headers
        val body = response.readErrorBody()

        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, url)

            headers.names().forEachIndexed { index, s ->
                val name = when (index) {
                    0 -> FirebaseAnalytics.Param.ITEM_CATEGORY2
                    1 -> FirebaseAnalytics.Param.ITEM_CATEGORY3
                    2 -> FirebaseAnalytics.Param.ITEM_CATEGORY4
                    3 -> FirebaseAnalytics.Param.ITEM_CATEGORY5
                    else -> null
                }

                name?.let {
                    putString(it, headers.value(index))
                }
            }

            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, body)
        }

        if (response.code == 401) {
            firebaseAnalytics.logEvent("logout", bundle)
        } else {
            firebaseAnalytics.logEvent("request", bundle)
        }

        firebaseCrashlytics.log(url)
        firebaseCrashlytics.log(headers.toString())
        firebaseCrashlytics.log(body)

        return response
    }
}

private fun Response.readErrorBody(): String {
    return try {
        peekBody(Long.MAX_VALUE).string()
    } catch (t: Throwable) {
        ""
    }
}

val Response.isSuccessfulOr3xx: Boolean
    get() {
        val code = code
        return code in 200..399
    }