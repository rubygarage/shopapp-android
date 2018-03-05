package com.shopapp.ui.checkout.payment.card.di

import com.shopapp.ui.checkout.payment.card.CardActivity
import dagger.Subcomponent

@Subcomponent(modules = [TestCardPaymentModule::class])
interface TestCardPaymentComponent: CardPaymentComponent {

    override fun inject(activity: CardActivity)
}