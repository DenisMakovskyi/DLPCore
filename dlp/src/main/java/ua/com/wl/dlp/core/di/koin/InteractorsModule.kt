package ua.com.wl.dlp.core.di.koin

import org.koin.dsl.module

import ua.com.wl.dlp.domain.interactors.*
import ua.com.wl.dlp.domain.interactors.impl.*

/**
 * @author Denis Makovskyi
 */

val interactorsModule = module {
    single<AuthInteractor> {
        AuthInteractorImpl(
            errorsMapper = get(),
            apiV1 = get(),
            apiV2 = get(),
            corePreferences = get(),
            consumerPreferences = get())
    }
    single<ConsumerInteractor> {
        ConsumerInteractorImpl(
            errorsMapper = get(),
            app = get(),
            apiV1 = get(),
            apiV2 = get(),
            shopsDataSource = get(),
            offersInteractor = get(),
            consumerPreferences = get())
    }
    single<NewsFeedInteractor> {
        NewsFeedInteractorImpl(
            errorsMapper = get(),
            app = get(),
            apiV1 = get(),
            consumerPreferences = get())
    }
    single<ShopInteractor> {
        ShopInteractorImpl(
            errorsMapper = get(),
            apiV1 = get(),
            apiV2 = get(),
            shopsDataSource = get(),
            offersInteractor = get())
    }
    single<PromotionInteractor> {
        PromotionInteractorImpl(
            errorsMapper = get(),
            apiV2 = get())
    }
    single<OffersInteractor> {
        OffersInteractorImpl(
            errorsMapper = get(),
            app = get(),
            apiV1 = get(),
            shopsDataSource = get(),
            consumerPreferences = get())
    }
    single<OrdersInteractor> {
        OrdersInteractorImpl(
            errorsMapper = get(),
            apiV1 = get(),
            apiV2 = get())
    }
}