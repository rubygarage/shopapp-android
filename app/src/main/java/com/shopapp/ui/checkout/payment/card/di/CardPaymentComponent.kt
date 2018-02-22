package com.shopapp.ui.checkout.payment.card.di

import com.shopapp.ui.checkout.payment.card.CardActivity
import dagger.Subcomponent

@Subcomponent(modules = [CardPaymentModule::class])
interface CardPaymentComponent {

    fun inject(activity: CardActivity)
}