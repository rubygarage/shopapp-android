package com.shopify.ui.address

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.domain.entity.Address
import com.shopify.ShopifyWrapper
import com.shopify.constant.Extra
import com.shopify.ui.address.di.AddressModule
import com.ui.module.address.BaseAddressActivity
import com.ui.module.address.contract.AddressPresenter
import com.ui.module.address.contract.AddressView

class CheckoutAddressActivity : BaseAddressActivity<AddressView, AddressPresenter<AddressView>>() {

    companion object {

        private const val ADDRESS = "address"
        private const val IS_SELECTED_ADDRESS = "is_selected_address"

        fun getStartIntent(
            context: Context,
            address: Address? = null,
            isSelectedAddress: Boolean = false
        ): Intent {
            val intent = Intent(context, CheckoutAddressActivity::class.java)
            intent.putExtra(ADDRESS, address)
            intent.putExtra(IS_SELECTED_ADDRESS, isSelectedAddress)
            return intent
        }
    }

    //INIT

    override fun inject() {
        ShopifyWrapper.component.attachAddressComponent(AddressModule()).inject(this)
    }

    //LCE

    override fun addressChanged(address: Address) {
        val result = Intent()
        result.putExtra(Extra.ADDRESS, address)
        result.putExtra(Extra.IS_SELECTED_ADDRESS,
            intent.getBooleanExtra(IS_SELECTED_ADDRESS, false))
        setResult(Activity.RESULT_OK, result)
        finish()
    }
}