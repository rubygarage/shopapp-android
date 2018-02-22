package com.shopapp.ui.address.account

import android.content.Context
import android.content.Intent
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView
import com.shopapp.ShopApplication
import com.shopapp.ui.address.base.BaseAddressActivity

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
        ShopApplication.appComponent.attachAddressComponent().inject(this)
    }
}