package com.client.shop.di.module

import com.apicore.Api
import com.repository.Repository
import com.repository.impl.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val shopApi: Api) {

    @Provides
    @Singleton
    fun provideRepository(): Repository {
        return RepositoryImpl(
                ShopRepositoryImpl(shopApi),
                BlogRepositoryImpl(shopApi),
                ProductRepositoryImpl(shopApi),
                CategoryRepositoryImpl(shopApi),
                CartRepositoryImpl(shopApi))
    }
}