package com.shopapp.ui.splash.di

import com.shopapp.ui.splash.router.SplashRouter
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    fun provideSplashRouter(): SplashRouter = SplashRouter()

}