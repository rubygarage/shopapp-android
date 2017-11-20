package com.shopify.ui.payment.di

import com.shopify.ui.payment.contract.CardPaymentPresenter
import com.shopify.ui.payment.contract.CardPaymentUseCase
import dagger.Module
import dagger.Provides

@Module
class PaymentModule {

    @Provides
    fun provideCardPaymentPresenter(cardPaymentUseCase: CardPaymentUseCase): CardPaymentPresenter =
            CardPaymentPresenter(cardPaymentUseCase)
}