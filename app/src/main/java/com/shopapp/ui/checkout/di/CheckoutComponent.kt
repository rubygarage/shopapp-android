package com.shopapp.ui.checkout.di

import com.shopapp.ui.checkout.CheckoutActivity
import dagger.Subcomponent

@Subcomponent(modules = [CheckoutModule::class])
interface CheckoutComponent {

    fun inject(activity: CheckoutActivity)
}