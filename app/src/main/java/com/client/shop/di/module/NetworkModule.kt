package com.client.shop.di.module

import com.shopapicore.ShopApi
import com.shopapicore.ShopApiCore
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule(private val shopApi: ShopApi) {

    @Provides
    @Singleton
    fun provideShopApiCore(): ShopApiCore {
        return ShopApiCore(shopApi)
    }
}