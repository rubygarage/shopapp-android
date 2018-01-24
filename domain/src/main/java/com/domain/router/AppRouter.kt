package com.domain.router

import android.app.Activity
import android.content.Context
import com.domain.entity.ProductVariant

interface AppRouter {

    fun openCartScreen(activity: Activity)

    fun openProductDetailsScreen(context: Context, productVariant: ProductVariant)
}