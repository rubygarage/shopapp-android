package com.shopapp.ui.account.router

import android.content.Context
import android.support.v4.app.Fragment
import com.shopapp.gateway.entity.Policy
import com.shopapp.ui.account.AccountSettingsActivity
import com.shopapp.ui.account.PersonalInfoActivity
import com.shopapp.ui.account.SignInActivity
import com.shopapp.ui.account.SignUpActivity
import com.shopapp.ui.address.account.AddressListActivity
import com.shopapp.ui.order.list.OrderListActivity
import com.shopapp.ui.policy.PolicyActivity

class AccountRouter {

    fun showPolicy(context: Context?, policy: Policy) {
        context?.let {
            it.startActivity(PolicyActivity.getStartIntent(it, policy))
        }
    }

    fun showOrderList(context: Context?) {
        context?.let {
            it.startActivity(OrderListActivity.getStartIntent(it))
        }
    }

    fun showAddressList(context: Context?) {
        context?.let { it.startActivity(AddressListActivity.getStartIntent(it)) }
    }

    fun showSettings(context: Context?) {
        context?.let { it.startActivity(AccountSettingsActivity.getStartIntent(it)) }
    }

    fun showPersonalInfoForResult(fragment: Fragment, requestCode: Int) {
        fragment.context?.let {
            fragment.startActivityForResult(PersonalInfoActivity.getStartIntent(it), requestCode)
        }
    }

    fun showSignInForResult(fragment: Fragment, requestCode: Int) {
        fragment.context?.let {
            fragment.startActivityForResult(SignInActivity.getStartIntent(it), requestCode)
        }
    }

    fun showSignUpForResult(fragment: Fragment, privacyPolicy: Policy?, termsOfService: Policy?, requestCode: Int) {
        fragment.context?.let {
            fragment.startActivityForResult(SignUpActivity.getStartIntent(it, privacyPolicy, termsOfService), requestCode)
        }
    }

}