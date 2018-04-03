package com.shopapp.ui.checkout.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.checkout.contract.CheckoutPresenter
import com.shopapp.ui.checkout.router.CheckoutRouter
import dagger.Module
import dagger.Provides

@Module
class TestCheckoutModule {

    @Provides
    fun provideCheckoutPresenter(): CheckoutPresenter = mock()

    @Provides
    fun provideCheckoutRouter(): CheckoutRouter = mock()
}