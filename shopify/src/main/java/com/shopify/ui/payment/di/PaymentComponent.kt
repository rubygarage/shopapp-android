package com.shopify.ui.payment.di

import com.shopify.ui.payment.CardPaymentActivity
import dagger.Subcomponent

@Subcomponent(modules = arrayOf(PaymentModule::class))
interface PaymentComponent {

    fun inject(activity: CardPaymentActivity)
}