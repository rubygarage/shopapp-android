package com.client.shop.di.module

import com.apicore.Api
import com.daocore.Dao
import com.repository.Repository
import com.repository.impl.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val api: Api, private val dao: Dao) {

    @Provides
    @Singleton
    fun provideRepository(): Repository {
        return RepositoryImpl(
                ShopRepositoryImpl(api),
                BlogRepositoryImpl(api),
                ProductRepositoryImpl(api),
                CategoryRepositoryImpl(api),
                CartRepositoryImpl(api, dao))
    }
}