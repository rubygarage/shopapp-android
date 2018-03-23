package com.shopapp.ui.checkout.payment.card.di

import com.nhaarman.mockito_kotlin.mock
import com.shopapp.ui.checkout.payment.card.contract.CardPresenter
import dagger.Module
import dagger.Provides

@Module
class TestCardPaymentModule {

    @Provides
    fun provideCardPresenter(): CardPresenter = mock()
}