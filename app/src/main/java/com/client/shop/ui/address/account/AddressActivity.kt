package com.client.shop.ui.address.account

import android.content.Context
import android.content.Intent
import com.client.shop.ShopApplication
import com.client.shop.ui.address.base.BaseAddressActivity
import com.client.shop.ui.address.account.di.AddressModule
import com.client.shop.getaway.entity.Address
import com.client.shop.ui.address.base.contract.AddressPresenter
import com.client.shop.ui.address.base.contract.AddressView

class AddressActivity : BaseAddressActivity<AddressView, AddressPresenter<AddressView>>() {

    companion object {

        private const val ADDRESS = "address"

        fun getStartIntent(context: Context, address: Address? = null): Intent {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra(ADDRESS, address)
            return intent
        }
    }

    override fun inject() {
        ShopApplication.appComponent.attachAddressComponent(AddressModule()).inject(this)
    }
}