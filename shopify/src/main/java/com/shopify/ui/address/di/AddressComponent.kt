package com.shopify.ui.address.di

import com.shopify.ui.address.AddressActivity
import com.shopify.ui.address.AddressListActivity
import dagger.Subcomponent

@Subcomponent(modules = [AddressModule::class])
interface AddressComponent {

    fun inject(activity: AddressActivity)

    fun inject(activity: AddressListActivity)
}