package com.client.shop.ui.address

import android.content.Context
import android.content.Intent
import com.client.shop.ShopApplication
import com.client.shop.ui.address.di.AddressModule
import com.domain.entity.Address
import com.ui.module.address.BaseAddressActivity
import com.ui.module.address.contract.AddressPresenter
import com.ui.module.address.contract.AddressView

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