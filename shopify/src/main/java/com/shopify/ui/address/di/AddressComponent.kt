package com.shopify.ui.address.di

import com.shopify.ui.address.CheckoutAddressActivity
import com.shopify.ui.address.CheckoutAddressListActivity
import com.shopify.ui.address.CheckoutUnAuthAddressActivity
import dagger.Subcomponent

@Subcomponent(modules = [AddressModule::class])
interface AddressComponent {

    fun inject(activity: CheckoutAddressListActivity)

    fun inject(activity: CheckoutAddressActivity)

    fun inject(activity: CheckoutUnAuthAddressActivity)
}