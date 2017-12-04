package com.client.shop.ui.home.di

import com.client.shop.ui.home.contract.HomePresenter
import com.domain.interactor.home.HomeUseCase
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    fun provideSplashPresenter(homeUseCase: HomeUseCase): HomePresenter {
        return HomePresenter(homeUseCase)
    }
}