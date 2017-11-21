package com.shopify.di

import com.domain.database.Dao
import com.repository.CartRepository
import com.repository.impl.CartRepositoryImpl
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