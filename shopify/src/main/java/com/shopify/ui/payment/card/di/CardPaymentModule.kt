package com.shopify.ui.payment.card.di

import com.shopify.ui.payment.card.contract.CardPaymentPresenter
import com.shopify.ui.payment.card.contract.CardPaymentUseCase
import dagger.Module
import dagger.Provides

@Module
class CardPaymentModule {

    @Provides
    fun provideCardPaymentPresenter(cardPaymentUseCase: CardPaymentUseCase): CardPaymentPresenter =
            CardPaymentPresenter(cardPaymentUseCase)
}