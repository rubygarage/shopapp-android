package com.client.shop.di.module

import com.domain.database.Dao
import com.domain.network.Api
import com.domain.repository.*
import com.data.impl.*
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
}