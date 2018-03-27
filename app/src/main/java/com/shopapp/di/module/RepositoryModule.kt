package com.shopapp.di.module

import com.shopapp.gateway.Api
import com.shopapp.data.impl.*
import com.shopapp.data.dao.Dao
import com.shopapp.domain.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val api: Api, private val dao: Dao) {

    @Provides
    @Singleton
    fun provideShopRepository(): ShopRepository = ShopRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideBlogRepository(): BlogRepository = BlogRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideProductRepository(): ProductRepository = ProductRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideCategoryRepository(): CategoryRepository = CategoryRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideOrderRepository(): OrderRepository = OrderRepositoryImpl(api)

    @Provides
    @Singleton
    fun provideCheckoutRepository(): CheckoutRepository = CheckoutRepositoryImpl(api)

}