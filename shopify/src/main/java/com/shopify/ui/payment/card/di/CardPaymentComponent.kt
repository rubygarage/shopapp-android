package com.shopify.ui.payment.card.di

import com.shopify.ui.payment.card.CardPaymentActivity
import dagger.Subcomponent

@Subcomponent(modules = [CardPaymentModule::class])
interface CardPaymentComponent {

    fun inject(activity: CardPaymentActivity)
}