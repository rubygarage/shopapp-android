package com.client.shop.ui.checkout.payment.card.di

import com.client.shop.ui.checkout.payment.card.contract.CardPresenter
import com.domain.interactor.checkout.CheckCreditCardUseCase
import com.domain.interactor.checkout.GetAcceptedCardTypesUseCase
import dagger.Module
import dagger.Provides

@Module
class CardPaymentModule {

    @Provides
    fun provideCardPresenter(
        checkCreditCardUseCase: CheckCreditCardUseCase,
        getAcceptedCardTypesUseCase: GetAcceptedCardTypesUseCase
    ): CardPresenter = CardPresenter(checkCreditCardUseCase, getAcceptedCardTypesUseCase)
}