package com.shopify.router

import android.app.Activity
import com.domain.router.Router
import com.shopify.ui.CheckoutActivity

class ShopifyRouter : Router {

    override fun startCheckoutFlow(activity: Activity) {
        activity.startActivity(CheckoutActivity.getStartIntent(activity))
    }
}