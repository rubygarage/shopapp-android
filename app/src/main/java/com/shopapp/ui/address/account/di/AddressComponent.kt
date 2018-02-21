package com.client.shop.ui.address.account.di

import com.client.shop.ui.address.account.AddressActivity
import com.client.shop.ui.address.account.AddressListActivity
import dagger.Subcomponent

@Subcomponent(modules = [AddressModule::class])
interface AddressComponent {

    fun inject(activity: AddressActivity)

    fun inject(activity: AddressListActivity)
}