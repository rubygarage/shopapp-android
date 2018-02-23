package com.shopapp.ui.cart.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.cart.contract.CartPresenter
import com.shopapp.ui.cart.contract.CartWidgetPresenter
import dagger.Module
import dagger.Provides

@Module
class TestCartModule {

    @Provides
    fun provideCartPresenter(): CartPresenter = mock()

    @Provides
    fun provideCartWidgetPresenter(): CartWidgetPresenter = mock()
}