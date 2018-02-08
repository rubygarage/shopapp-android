package com.shopify.di.module

import com.domain.router.AppRouter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RouterModule(private val appRouter: AppRouter) {

    @Provides
    @Singleton
    fun provideRouter(): AppRouter {
        return appRouter
    }
}