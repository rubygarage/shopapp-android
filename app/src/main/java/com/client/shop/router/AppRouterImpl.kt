package com.client.shop.router

import android.app.Activity
import android.content.Context
import com.client.shop.ui.cart.CartActivity
import com.client.shop.ui.product.ProductDetailsActivity
import com.domain.entity.ProductVariant
import com.domain.router.AppRouter

class AppRouterImpl : AppRouter {

    override fun openCartScreen(activity: Activity) {
        activity.startActivity(CartActivity.getStartIntent(activity))
    }

    override fun openProductDetailsScreen(context: Context, productVariant: ProductVariant) {
        context.startActivity(ProductDetailsActivity.getStartIntent(context, productVariant))
    }
}