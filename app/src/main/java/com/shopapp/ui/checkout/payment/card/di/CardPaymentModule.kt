package com.shopapp.ui.checkout.payment.card.di

import com.shopapp.domain.interactor.checkout.CheckCreditCardUseCase
import com.shopapp.domain.interactor.checkout.GetAcceptedCardTypesUseCase
import com.shopapp.ui.checkout.payment.card.contract.CardPresenter
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