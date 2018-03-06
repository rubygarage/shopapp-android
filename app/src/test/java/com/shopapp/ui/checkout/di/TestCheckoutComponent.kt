package com.shopapp.ui.checkout.di

import com.shopapp.ui.checkout.CheckoutActivity
import dagger.Subcomponent

@Subcomponent(modules = [TestCheckoutModule::class])
interface TestCheckoutComponent : CheckoutComponent {

    override fun inject(activity: CheckoutActivity)
}