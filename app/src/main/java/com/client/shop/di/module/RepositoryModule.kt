package com.client.shop.di.module

import com.client.shop.getaway.Api
import com.data.impl.*
import com.domain.database.Dao
import com.domain.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
open class RepositoryModule(private val api: Api, private val dao: Dao) {

    @Provides
    @Singleton
    open fun provideShopRepository(): ShopRepository = ShopRepositoryImpl(api)

    @Provides
    @Singleton
    open fun provideBlogRepository(): BlogRepository = BlogRepositoryImpl(api)

    @Provides
    @Singleton
    open fun provideProductRepository(): ProductRepository = ProductRepositoryImpl(api)

    @Provides
    @Singleton
    open fun provideCategoryRepository(): CategoryRepository = CategoryRepositoryImpl(api)

    @Provides
    @Singleton
    open fun provideCartRepository(): CartRepository = CartRepositoryImpl(dao)

    @Provides
    @Singleton
    open fun provideAuthRepository(): AuthRepository = AuthRepositoryImpl(api)

    @Provides
    @Singleton
    open fun provideOrderRepository(): OrderRepository = OrderRepositoryImpl(api)

    @Provides
    @Singleton
    open fun provideCheckoutRepository(): CheckoutRepository = CheckoutRepositoryImpl(api)

}