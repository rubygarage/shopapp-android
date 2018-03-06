package com.shopapp.ui.checkout.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.checkout.contract.CheckoutPresenter
import dagger.Module
import dagger.Provides

@Module
class TestCheckoutModule {

    @Provides
    fun provideCheckoutPresenter(): CheckoutPresenter = mock()
}