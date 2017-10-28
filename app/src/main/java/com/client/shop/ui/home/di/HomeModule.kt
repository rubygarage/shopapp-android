package com.client.shop.ui.home.di

import com.client.shop.ui.home.contract.HomePresenter
import com.repository.Repository
import dagger.Module
import dagger.Provides

@Module
class HomeModule {

    @Provides
    fun provideSplashPresenter(repository: Repository): HomePresenter {
        return HomePresenter(repository)
    }
}