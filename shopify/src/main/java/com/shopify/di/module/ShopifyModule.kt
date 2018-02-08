package com.shopify.di.module

import com.data.impl.AuthRepositoryImpl
import com.data.impl.CartRepositoryImpl
import com.domain.database.Dao
import com.domain.repository.AuthRepository
import com.domain.repository.CartRepository
import com.domain.validator.FieldValidator
import com.shopify.api.ShopifyApi
import com.shopify.repository.CheckoutRepository
import com.shopify.repository.impl.CheckoutRepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ShopifyModule(private val api: ShopifyApi, private val dao: Dao) {

    @Provides
    fun provideFieldValidator() = FieldValidator()

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideCheckoutRepository(): CheckoutRepository = CheckoutRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(api)
}