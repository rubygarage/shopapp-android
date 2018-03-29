package com.shopapp.ui.base.router

import android.app.Activity

interface Router {

    fun <T: Activity> start()
}