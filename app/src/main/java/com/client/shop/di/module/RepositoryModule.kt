package com.client.shop.di.module

import com.apicore.Api
import com.daocore.Dao
import com.repository.*
import com.repository.impl.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val api: Api, private val dao: Dao) {

    @Provides
    @Singleton
    fun provideShopRepository(): ShopRepository {
        return ShopRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideBlogRepository(): BlogRepository {
        return BlogRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideProductRepository(): ProductRepository {
        return ProductRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(): CategoryRepository {
        return CategoryRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideCartRepository(): CartRepository {
        return CartRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepository {
        return AuthRepositoryImpl(api)
    }
}