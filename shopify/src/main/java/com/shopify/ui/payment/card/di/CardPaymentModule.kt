package com.shopify.ui.payment.card.di

import com.shopify.interactor.checkout.CheckCreditCardUseCase
import com.shopify.ui.payment.card.contract.CardPresenter
import dagger.Module
import dagger.Provides

@Module
class CardPaymentModule {

    @Provides
    fun provideCardPresenter(checkCreditCardUseCase: CheckCreditCardUseCase): CardPresenter =
        CardPresenter(checkCreditCardUseCase)
}