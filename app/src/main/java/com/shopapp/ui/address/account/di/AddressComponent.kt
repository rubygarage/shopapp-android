package com.shopapp.ui.address.account.di

import com.shopapp.ui.address.account.AddressActivity
import com.shopapp.ui.address.account.AddressListActivity
import dagger.Subcomponent

@Subcomponent(modules = [AddressModule::class])
interface AddressComponent {

    fun inject(activity: AddressActivity)

    fun inject(activity: AddressListActivity)
}