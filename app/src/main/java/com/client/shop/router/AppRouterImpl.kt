package com.client.shop.router

import android.app.Activity
import com.client.shop.ui.cart.CartActivity
import com.client.shop.ui.details.DetailsActivity
import com.domain.router.AppRouter

class AppRouterImpl : AppRouter {

    override fun openCartScreen(activity: Activity) {
        activity.startActivity(CartActivity.getStartIntent(activity))
    }

    override fun openProductDetailsScreen(activity: Activity, productId: String) {
        activity.startActivity(DetailsActivity.getStartIntent(activity, productId))
    }
}