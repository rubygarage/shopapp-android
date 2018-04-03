package com.shopapp.ui.address.account.router

import android.app.Activity
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Policy
import com.shopapp.ui.account.AccountFragment
import com.shopapp.ui.account.SignInActivity
import com.shopapp.ui.account.SignUpActivity
import com.shopapp.ui.address.account.AddressActivity
import com.shopapp.ui.address.account.AddressListActivity

class AddressListRouter {

    fun showAddressForResult(activity: Activity, requestCode: Int, address: Address? = null) {
        activity.startActivityForResult(AddressActivity.getStartIntent(activity, address), requestCode)
    }

}