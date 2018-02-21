package com.client.shop.ui.checkout.di

import com.client.shop.ui.checkout.CheckoutActivity
import com.client.shop.ui.checkout.view.CheckoutMyCartView
import dagger.Subcomponent

@Subcomponent(modules = [CheckoutModule::class])
interface CheckoutComponent {

    fun inject(activity: CheckoutActivity)

    fun inject(view: CheckoutMyCartView)
}