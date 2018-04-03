package com.shopapp.ui.splash.router

import android.content.Context
import com.shopapp.ui.home.HomeActivity

class SplashRouter {

    fun showHome(context: Context?, isNewTask: Boolean = false) {
        context?.let { it.startActivity(HomeActivity.getStartIntent(it, isNewTask)) }
    }

}