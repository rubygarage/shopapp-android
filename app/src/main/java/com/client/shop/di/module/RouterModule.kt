package com.client.shop.di.module

import com.domain.ShopWrapper
import com.domain.router.Router
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RouterModule(private val shopWrapper: ShopWrapper) {

    @Provides
    @Singleton
    fun provideRouter(): Router {
        return shopWrapper.router
    }
}