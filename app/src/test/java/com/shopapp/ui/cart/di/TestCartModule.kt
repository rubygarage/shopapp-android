package com.shopapp.ui.cart.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.cart.contract.CartPresenter
import com.shopapp.ui.cart.contract.CartWidgetPresenter
import com.shopapp.ui.cart.router.CartRouter
import com.shopapp.ui.cart.router.CartWidgetRouter
import dagger.Module
import dagger.Provides

@Module
class TestCartModule {

    @Provides
    fun provideCartPresenter(): CartPresenter = mock()

    @Provides
    fun provideCartWidgetPresenter(): CartWidgetPresenter = mock()

    @Provides
    fun provideCartRouter(): CartRouter = mock()

    @Provides
    fun provideCartWidgetRouter(): CartWidgetRouter = mock()
}