package com.client.shop.ui.cart.di

import com.client.shop.ui.cart.contract.CartPresenter
import com.client.shop.ui.cart.contract.CartWidgetPresenter
import com.nhaarman.mockito_kotlin.mock
import dagger.Module
import dagger.Provides

@Module
class TestCartModule {

    @Provides
    fun provideCartPresenter(): CartPresenter = mock()

    @Provides
    fun provideCartWidgetPresenter(): CartWidgetPresenter = mock()
}