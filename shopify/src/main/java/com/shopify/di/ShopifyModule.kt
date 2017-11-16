package com.shopify.di

import com.domain.database.Dao
import com.domain.network.Api
import com.repository.CartRepository
import com.repository.CheckoutRepository
import com.repository.impl.CartRepositoryImpl
import com.repository.impl.CheckoutRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ShopifyModule(private val api: Api, private val dao: Dao) {

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCheckoutRepository(): CheckoutRepository = CheckoutRepositoryImpl(api)
}