package com.client.shop.router

import android.app.Activity
import com.client.shop.ui.cart.CartActivity
import com.client.shop.ui.product.ProductDetailsActivity
import com.domain.router.AppRouter

class AppRouterImpl : AppRouter {

    override fun openCartScreen(activity: Activity) {
        activity.startActivity(CartActivity.getStartIntent(activity))
    }

    override fun openProductDetailsScreen(activity: Activity, productId: String) {
        activity.startActivity(ProductDetailsActivity.getStartIntent(activity, productId))
    }
}