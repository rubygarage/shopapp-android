package com.shopapp.ui.splash.di

import com.shopapp.domain.interactor.cart.CartValidationUseCase
import com.shopapp.ui.splash.contract.SplashPresenter
import com.shopapp.ui.splash.router.SplashRouter
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    fun provideSplashRouter(): SplashRouter = SplashRouter()

    @Provides
    fun provideSplashPresenter(cartValidationUseCase: CartValidationUseCase) =
        SplashPresenter(cartValidationUseCase)

}