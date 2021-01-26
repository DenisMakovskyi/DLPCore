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
        val headers = request.headers.toString()
        val body = response.readErrorBody()

        if (response.code == 401) {
            val bundle = Bundle().apply {
                putString(FirebaseAnalytics.Param.METHOD, url)
                putString(FirebaseAnalytics.Param.CONTENT, headers)
                putString(FirebaseAnalytics.Param.ITEM_CATEGORY, body)
            }

            firebaseAnalytics.logEvent("logout", bundle)
        }

        firebaseCrashlytics.log(url)
        firebaseCrashlytics.log(headers)
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