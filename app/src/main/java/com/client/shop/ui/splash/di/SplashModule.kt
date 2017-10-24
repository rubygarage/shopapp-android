package com.client.shop.ui.splash.di

import com.client.shop.ui.splash.contract.SplashPresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class SplashModule {

    @Provides
    fun provideSplashPresenter(repository: Repository): SplashPresenter {
        return SplashPresenter(repository)
    }
}