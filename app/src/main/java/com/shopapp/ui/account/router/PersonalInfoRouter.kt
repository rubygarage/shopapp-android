package com.shopapp.ui.account.router

import android.content.Context
import com.shopapp.ui.account.ChangePasswordActivity

class PersonalInfoRouter {

    fun showChangePassword(context: Context) {
        context.startActivity(ChangePasswordActivity.getStartIntent(context))
    }
}