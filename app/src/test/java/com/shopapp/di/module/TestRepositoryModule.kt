package com.shopapp.di.module

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.domain.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestRepositoryModule {

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