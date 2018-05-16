package com.shopapp.ui.home.di

import com.shopapp.domain.interactor.shop.ConfigUseCase
import com.shopapp.ui.home.contract.HomePresenter
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    fun provideHomePresenter(configUseCase: ConfigUseCase) = HomePresenter(configUseCase)
}