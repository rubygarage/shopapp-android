package com.shopapp.ui.address.account

import android.content.Context
import android.content.Intent
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.base.BaseAddressActivity
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView
import com.shopapp.ui.const.Extra

class AddressActivity : BaseAddressActivity<AddressView, AddressPresenter<AddressView>>() {

    companion object {

        fun getStartIntent(context: Context, address: Address? = null): Intent {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra(Extra.ADDRESS, address)
            return intent
        }
    }

    override fun inject() {
        ShopApplication.appComponent.attachAddressComponent().inject(this)
    }
}