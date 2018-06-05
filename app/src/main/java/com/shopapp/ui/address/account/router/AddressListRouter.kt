package com.shopapp.ui.address.account.router

import android.app.Activity
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.account.AddressActivity

class AddressListRouter {

    fun showAddressForResult(activity: Activity, requestCode: Int, address: Address? = null) {
        activity.startActivityForResult(
            AddressActivity.getStartIntent(activity, address),
            requestCode
        )
    }

}