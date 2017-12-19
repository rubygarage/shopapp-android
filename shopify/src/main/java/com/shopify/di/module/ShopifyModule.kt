package com.shopify.di.module

import com.data.impl.CartRepositoryImpl
import com.domain.database.Dao
import com.domain.repository.CartRepository
import com.shopify.api.ShopifyApi
import com.shopify.repository.CheckoutRepository
import com.shopify.repository.impl.CheckoutRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ShopifyModule(private val api: ShopifyApi, private val dao: Dao) {

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCheckoutRepository(): CheckoutRepository = CheckoutRepositoryImpl(api)
}