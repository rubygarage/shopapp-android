package com.shopapp.ui.account.router

import android.content.Context
import com.shopapp.ui.account.ForgotPasswordActivity

class SignInRouter {

    fun showForgotScreen(context: Context) {
        context.startActivity(ForgotPasswordActivity.getStartIntent(context))
    }
}