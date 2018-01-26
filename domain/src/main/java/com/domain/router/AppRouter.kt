package com.domain.router

import android.content.Context
import com.domain.entity.ProductVariant

interface AppRouter {

    fun openProductDetailsScreen(context: Context, productVariant: ProductVariant)

    fun openOrderSuccessScreen(context: Context, orderId: String, orderNumber: Int)
}