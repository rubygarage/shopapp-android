package com.shopapp.ui.checkout.di

import com.shopapp.ui.checkout.view.CheckoutMyCartView
import com.shopapp.ui.checkout.CheckoutActivity
import dagger.Subcomponent

@Subcomponent(modules = [CheckoutModule::class])
interface CheckoutComponent {

    fun inject(activity: CheckoutActivity)

    fun inject(view: CheckoutMyCartView)
}