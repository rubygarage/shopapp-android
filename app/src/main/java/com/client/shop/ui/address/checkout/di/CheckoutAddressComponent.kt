package com.client.shop.ui.address.checkout.di

import com.client.shop.ui.address.checkout.CheckoutAddressActivity
import com.client.shop.ui.address.checkout.CheckoutAddressListActivity
import com.client.shop.ui.address.checkout.CheckoutUnAuthAddressActivity
import dagger.Subcomponent

@Subcomponent(modules = [CheckoutAddressModule::class])
interface CheckoutAddressComponent {

    fun inject(activity: CheckoutAddressListActivity)

    fun inject(activity: CheckoutAddressActivity)

    fun inject(activity: CheckoutUnAuthAddressActivity)
}