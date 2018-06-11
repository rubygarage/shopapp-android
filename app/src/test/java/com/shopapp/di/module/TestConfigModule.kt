package com.shopapp.di.module

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.gateway.entity.Config
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestConfigModule {

    companion object {
        val config: Config = mock()
    }

    @Provides
    @Singleton
    fun provideConfigData(): Config = config
}