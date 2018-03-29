package com.shopapp.ui.account.router

import com.shopapp.gateway.entity.Policy
import com.shopapp.ui.account.*
import com.shopapp.ui.address.account.AddressListActivity
import com.shopapp.ui.order.list.OrderListActivity
import com.shopapp.ui.policy.PolicyActivity

class AccountRouter {

    fun showPolicy(fragment: AccountFragment, policy: Policy) {
        fragment.context?.let {
            it.startActivity(PolicyActivity.getStartIntent(it, policy))
        }
    }

    fun showSignInForResult(fragment: AccountFragment, requestCode: Int) {
        fragment.context?.let {
            fragment.startActivityForResult(SignInActivity.getStartIntent(it), requestCode)
        }
    }

    fun showSignUpForResult(fragment: AccountFragment, privacyPolicy: Policy?, termsOfService: Policy?, requestCode: Int) {
        fragment.context?.let {
            fragment.startActivityForResult(SignUpActivity.getStartIntent(it, privacyPolicy, termsOfService), requestCode)
        }
    }

    fun showOrderList(fragment: AccountFragment) {
        fragment.context?.let {
            it.startActivity(OrderListActivity.getStartIntent(it))
        }
    }

    fun showPersonalInfoForResult(fragment: AccountFragment, requestCode: Int) {
        fragment.context?.let {
            fragment.startActivityForResult(PersonalInfoActivity.getStartIntent(it), requestCode)
        }
    }

    fun showAddressList(fragment: AccountFragment) {
        fragment.context?.let {
            it.startActivity(AddressListActivity.getStartIntent(it))
        }
    }

    fun showSettings(fragment: AccountFragment) {
        fragment.context?.let {
            it.startActivity(AccountSettingsActivity.getStartIntent(it))
        }
    }
}