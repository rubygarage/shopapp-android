package com.shopapp.ui.address.checkout.di

import com.shopapp.ui.address.checkout.CheckoutAddressActivity
import com.shopapp.ui.address.checkout.CheckoutAddressListActivity
import com.shopapp.ui.address.checkout.CheckoutUnAuthAddressActivity
import dagger.Subcomponent

@Subcomponent(modules = [TestCheckoutAddressModule::class])
interface TestCheckoutAddressComponent : CheckoutAddressComponent {

    override fun inject(activity: CheckoutAddressListActivity)

    override fun inject(activity: CheckoutAddressActivity)

    override fun inject(activity: CheckoutUnAuthAddressActivity)
}