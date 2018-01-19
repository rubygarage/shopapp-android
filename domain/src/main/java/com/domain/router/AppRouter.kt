package com.domain.router

import android.app.Activity
import com.domain.entity.ProductVariant

interface AppRouter {

    fun openCartScreen(activity: Activity)

    fun openProductDetailsScreen(activity: Activity, productVariant: ProductVariant)
}