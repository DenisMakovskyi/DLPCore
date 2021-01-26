package ua.com.wl.dlp.core.di.koin

import android.content.pm.PackageManager
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ua.com.wl.dlp.core.Constants
import ua.com.wl.dlp.core.DLPCore
import ua.com.wl.dlp.core.network.AnalyticsInterceptor
import ua.com.wl.dlp.core.network.AuthInterceptor
import ua.com.wl.dlp.core.network.SessionAuthenticator
import ua.com.wl.dlp.data.api.*
import ua.com.wl.dlp.data.api.errors.ErrorsMapper
import java.util.concurrent.TimeUnit

/**
 * @author Denis Makovskyi
 */

val apiModule = module {
    // - Host url and app id from manifest meta data
    factory(qualifier = named(Constants.DI_NAMED_URL)) {
        val key = when(DLPCore.environment) {
            DLPCore.Environment.DEVELOPING -> Constants.META_STAGING_URL
            DLPCore.Environment.PRODUCTION -> Constants.META_PRODUCTION_URL
        }
        androidContext()
            .packageManager.getApplicationInfo(androidContext().packageName, PackageManager.GET_META_DATA)
            .metaData?.getString(key) ?: throw IllegalStateException("Server url was not found")
    }
    factory(qualifier = named(Constants.DI_NAMED_APP_IDS)) {
        val key = when(DLPCore.environment) {
            DLPCore.Environment.DEVELOPING -> Constants.META_STAGING_APP_IDS
            DLPCore.Environment.PRODUCTION -> Constants.META_PRODUCTION_APP_IDS
        }
        val metaValue = androidContext()
            .packageManager.getApplicationInfo(androidContext().packageName, PackageManager.GET_META_DATA)
            .metaData.get(key)
        when(metaValue) {
            is Int -> androidContext().resources.getStringArray(metaValue).toList()
            is String -> listOf(metaValue)
            else -> throw IllegalStateException("Application id was not found")
        }
    }
    // - Interceptors
    factory {
        AuthInterceptor(
            appIds = get(qualifier = named(Constants.DI_NAMED_APP_IDS)),
            corePreferences = get(),
            retrofit = get(qualifier = named(Constants.DI_NAMED_REFRESH_RETROFIT))
        )
    }

    factory {
        AnalyticsInterceptor(androidContext())
    }

    factory {
        HttpLoggingInterceptor().apply {
            level = if (DLPCore.debuggable) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    // - OKHTTP client for session retrofit
    factory(qualifier = named(Constants.DI_NAMED_SESSION_OKHTTP)) {
        OkHttpClient.Builder()
            .connectTimeout(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS)
            .readTimeout(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(interceptor = get<AuthInterceptor>())
            .addInterceptor(interceptor = get<HttpLoggingInterceptor>())
            .addInterceptor(interceptor = get<AnalyticsInterceptor>())
            .apply {
                if (DLPCore.debuggable) {
                    addInterceptor(interceptor = ChuckerInterceptor(androidContext()))
                }
            }
            .build()
    }
    factory(qualifier = named(Constants.DI_NAMED_REFRESH_OKHTTP)) {
        OkHttpClient.Builder()
            .connectTimeout(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS)
            .readTimeout(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(interceptor = get<HttpLoggingInterceptor>())
            .addInterceptor(interceptor = get<AnalyticsInterceptor>())
            .apply {
                if (DLPCore.debuggable) {
                    addInterceptor(interceptor = ChuckerInterceptor(androidContext()))
                }
            }
            .build()
    }
    // - Session retrofit - for anonymous api calls
    factory(qualifier = named(Constants.DI_NAMED_SESSION_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(get<String>(qualifier = named(Constants.DI_NAMED_URL)))
            .client(get<OkHttpClient>(qualifier = named(Constants.DI_NAMED_SESSION_OKHTTP)))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    factory(qualifier = named(Constants.DI_NAMED_REFRESH_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(get<String>(qualifier = named(Constants.DI_NAMED_URL)))
            .client(get<OkHttpClient>(qualifier = named(Constants.DI_NAMED_REFRESH_OKHTTP)))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    // - Authenticator
    factory {
        SessionAuthenticator(
            retrofit = get(qualifier = named(Constants.DI_NAMED_SESSION_RETROFIT)),
            authInterceptor = get(),
            corePreferences = get())
    }
    // - OKHTTP client for api retrofit
    factory(qualifier = named(Constants.DI_NAMED_API_OKHTTP)) {
        OkHttpClient.Builder()
            .connectTimeout(TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS)
            .readTimeout(TimeUnit.SECONDS.toMillis(30), TimeUnit.SECONDS)
            .followRedirects(false)
            .followSslRedirects(false)
            .addInterceptor(interceptor = get<AuthInterceptor>())
            .addInterceptor(interceptor = get<HttpLoggingInterceptor>())
            .authenticator(authenticator = get<SessionAuthenticator>())
            .addInterceptor(interceptor = get<AnalyticsInterceptor>())
            .apply {
                if (DLPCore.debuggable) {
                    addInterceptor(interceptor = ChuckerInterceptor(androidContext()))
                }
            }
            .build()
    }
    // - Api retrofit - for regular apis
    single(qualifier = named(Constants.DI_NAMED_API_RETROFIT)) {
        Retrofit.Builder()
            .baseUrl(get<String>(qualifier = named(Constants.DI_NAMED_URL)))
            .client(get<OkHttpClient>(qualifier = named(Constants.DI_NAMED_API_OKHTTP)))
            .addConverterFactory(GsonConverterFactory.create(get()))
            .build()
    }
    // - Apis implementations
    factory {
        ErrorsMapper(gson = get())
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(AuthApiV1::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(AuthApiV2::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(ConsumerApiV1::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(ConsumerApiV2::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(NewsApiV1::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(ShopApiV1::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(ShopApiV2::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(PromotionApiV2::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(OffersApiV1::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(OrdersApiV1::class.java)
    }
    factory {
        get<Retrofit>(qualifier = named(Constants.DI_NAMED_API_RETROFIT))
            .create(OrdersApiV2::class.java)
    }
}