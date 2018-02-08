package com.shopify.ui.checkout.di

import com.shopify.ui.checkout.CheckoutActivity
import com.shopify.ui.checkout.view.CheckoutMyCartView
import dagger.Subcomponent

@Subcomponent(modules = [CheckoutModule::class])
interface CheckoutComponent {

    fun inject(activity: CheckoutActivity)

    fun inject(view: CheckoutMyCartView)
}