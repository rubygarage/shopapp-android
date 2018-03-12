package com.shopapp.ui.address.account.di

import com.shopapp.ui.address.account.AddressActivity
import com.shopapp.ui.address.account.AddressListActivity
import com.shopapp.ui.address.base.BaseAddressActivityTest
import com.shopapp.ui.address.base.BaseAddressListActivityTest
import dagger.Subcomponent

@Subcomponent(modules = [TestAddressModule::class])
interface TestAddressComponent : AddressComponent {

    override fun inject(activity: AddressActivity)

    override fun inject(activity: AddressListActivity)

    fun inject(activity: BaseAddressListActivityTest.TestBaseAddressListActivity)

    fun inject(activity: BaseAddressActivityTest.TestBaseAddressActivity)
}