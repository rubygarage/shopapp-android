package com.client.shop.di.module

import com.repository.Repository
import com.repository.impl.RepositoryImpl
import com.apicore.Api
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule(private val shopApi: Api) {

    @Provides
    @Singleton
    fun provideRepository(): Repository {
        return RepositoryImpl(shopApi)
    }
}