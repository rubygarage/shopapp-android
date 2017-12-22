package com.shopify.ui.address.di

import com.shopify.ui.address.AddressActivity
import dagger.Subcomponent

@Subcomponent(modules = [AddressModule::class])
interface AddressComponent {

    fun inject(activity: AddressActivity)
}