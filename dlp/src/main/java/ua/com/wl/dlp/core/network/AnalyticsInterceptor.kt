package ua.com.wl.dlp.core.network

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import kotlinx.android.parcel.Parcelize
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.internal.toHeaderList

class AnalyticsInterceptor(
    context: Context
) : Interceptor {

    @Parcelize
    private data class Header(val name: String, val value: String) : Parcelable

    private val firebaseAnalytics = FirebaseAnalytics.getInstance(context)
    private val firebaseCrashlytics = FirebaseCrashlytics.getInstance()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.isSuccessfulOr3xx) {
            return response
        }

        val url = request.url.encodedPath
        val headers = request.headers.toHeaderList().map { Header(it.name.utf8(), it.value.utf8()) }
        val body = response.readErrorBody()

        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, url)
            putParcelableArray(FirebaseAnalytics.Param.ITEMS, headers.toTypedArray())
            putString(FirebaseAnalytics.Param.ITEM_CATEGORY, body)
        }

        if (response.code == 401) {
            firebaseAnalytics.logEvent("logout", bundle)
        } else {
            firebaseAnalytics.logEvent("request", bundle)
        }

        firebaseCrashlytics.log(url)
        firebaseCrashlytics.log(headers.joinToString { it.name + ":" + it.value })
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