package com.shopapp.ui.account.router

import android.content.Context
import com.shopapp.ui.account.ChangePasswordActivity

class PersonalInfoRouter {

    fun showChangePassword(context: Context?) {
        context?.let { it.startActivity(ChangePasswordActivity.getStartIntent(it)) }
    }
}