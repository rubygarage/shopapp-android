package com.domain.router

import android.app.Activity

interface AppRouter {

    fun openCartScreen(activity: Activity)

    fun openProductDetailsScreen(activity: Activity, productId: String)
}