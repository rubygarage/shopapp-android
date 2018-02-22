package com.shopapp.di.module

import com.shopapp.gateway.Api
import com.shopapp.domain.database.Dao
import com.shopapp.domain.repository.*
import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRepositoryModule(private val api: Api, private val dao: Dao)  {

    @Provides
    @Singleton
    fun provideShopRepository(): ShopRepository = mock()

    @Provides
    @Singleton
    fun provideBlogRepository(): BlogRepository = mock()

    @Provides
    @Singleton
    fun provideProductRepository(): ProductRepository = mock()

    @Provides
    @Singleton
    fun provideCategoryRepository(): CategoryRepository = mock()

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository = mock()

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = mock()

    @Provides
    @Singleton
    fun provideOrderRepository(): OrderRepository = mock()

    @Provides
    @Singleton
    fun provideCheckoutRepository(): CheckoutRepository = mock()

}