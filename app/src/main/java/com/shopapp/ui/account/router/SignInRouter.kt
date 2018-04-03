package com.shopapp.ui.account.router

import android.content.Context
import com.shopapp.ui.account.ForgotPasswordActivity

class SignInRouter {

    fun showForgotPassword(context: Context?) {
        context?.let { it.startActivity(ForgotPasswordActivity.getStartIntent(it)) }
    }
}