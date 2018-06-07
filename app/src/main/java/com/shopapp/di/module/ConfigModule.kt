package com.shopapp.di.module

import com.shopapp.gateway.entity.Config
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ConfigModule(private val config: Config) {

    @Provides
    @Singleton
    fun provideConfigData(): Config = config
}