package com.shopapp.ui.address.checkout.router

import android.app.Activity
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.checkout.CheckoutAddressActivity

class CheckoutAddressRouter {

    fun showCheckoutAddressForResult(activity: Activity,
                                     requestCode: Int,
                                     address: Address? = null,
                                     isSelectedAddress: Boolean = false) {

        val intent = CheckoutAddressActivity.getStartIntent(activity, address, isSelectedAddress)
        activity.startActivityForResult(intent, requestCode)
    }
}