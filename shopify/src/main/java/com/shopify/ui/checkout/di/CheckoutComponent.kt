package com.shopify.ui.checkout.di

import com.shopify.ui.checkout.CheckoutActivity
import dagger.Subcomponent

@Subcomponent(modules = [CheckoutModule::class])
interface CheckoutComponent {

    fun inject(activity: CheckoutActivity)
}