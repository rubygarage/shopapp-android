package com.shopapp.ui.address.checkout.di

import com.shopapp.ui.address.checkout.CheckoutAddressActivity
import com.shopapp.ui.address.checkout.CheckoutAddressListActivity
import com.shopapp.ui.address.checkout.CheckoutUnAuthAddressActivity
import dagger.Subcomponent

@Subcomponent(modules = [CheckoutAddressModule::class])
interface CheckoutAddressComponent {

    fun inject(activity: CheckoutAddressListActivity)

    fun inject(activity: CheckoutAddressActivity)

    fun inject(activity: CheckoutUnAuthAddressActivity)
}