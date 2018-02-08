package com.client.shop.ui.address.di

import com.client.shop.ui.address.AddressActivity
import com.client.shop.ui.address.AddressListActivity
import dagger.Subcomponent

@Subcomponent(modules = [AddressModule::class])
interface AddressComponent {

    fun inject(activity: AddressActivity)

    fun inject(activity: AddressListActivity)
}