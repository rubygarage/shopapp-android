package com.shopapp.ui.splash.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.splash.contract.SplashPresenter
import com.shopapp.ui.splash.router.SplashRouter
import dagger.Module
import dagger.Provides

@Module
class TestSplashModule {

    @Provides
    fun provideSplashRouter(): SplashRouter = mock()

    @Provides
    fun provideSplashPresenter(): SplashPresenter = mock()

}