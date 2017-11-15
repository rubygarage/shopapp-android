package com.client.shop.di.module

import com.domain.ShopModule
import com.domain.router.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RouterModule(private val shopModule: ShopModule) {

    @Provides
    @Singleton
    fun provideRouter(): Router {
        return shopModule.router
    }
}