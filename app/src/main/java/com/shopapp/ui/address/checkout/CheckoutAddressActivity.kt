package com.shopapp.ui.address.checkout

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.base.BaseAddressActivity
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView
import com.shopapp.ui.const.Extra

class CheckoutAddressActivity : BaseAddressActivity<AddressView, AddressPresenter<AddressView>>() {

    companion object {

        fun getStartIntent(
            context: Context,
            address: Address? = null,
            isSelectedAddress: Boolean = false
        ): Intent {
            val intent = Intent(context, CheckoutAddressActivity::class.java)
            intent.putExtra(Extra.ADDRESS, address)
            intent.putExtra(Extra.IS_SELECTED_ADDRESS, isSelectedAddress)
            return intent
        }
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachCheckoutAddressComponent().inject(this)
    }

    //LCE

    override fun addressChanged(address: Address) {
        val result = Intent()
        result.putExtra(Extra.ADDRESS, address)
        result.putExtra(Extra.IS_SELECTED_ADDRESS,
            intent.getBooleanExtra(Extra.IS_SELECTED_ADDRESS, false))
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}