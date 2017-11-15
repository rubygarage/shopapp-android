package com.shopify.di

import com.domain.database.Dao
import com.domain.network.Api
import com.repository.CartRepository
import com.repository.impl.CartRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CheckoutModule(private val api: Api, private val dao: Dao) {

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository = CartRepositoryImpl(dao)
}