package com.shopapp.ui.account.router

import android.content.Context
import com.shopapp.gateway.entity.Policy
import com.shopapp.ui.policy.PolicyActivity

class SignUpRouter {

    fun showPolicy(context: Context?, policy: Policy) {
        context?.let { it.startActivity(PolicyActivity.getStartIntent(it, policy)) }
    }
}