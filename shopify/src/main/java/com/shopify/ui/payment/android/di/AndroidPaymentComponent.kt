package com.shopify.ui.payment.android.di

import com.shopify.ui.payment.android.AndroidPaymentActivity
import dagger.Subcomponent

@Subcomponent(modules = [AndroidPaymentModule::class])
interface AndroidPaymentComponent {

    fun inject(activity: AndroidPaymentActivity)
}