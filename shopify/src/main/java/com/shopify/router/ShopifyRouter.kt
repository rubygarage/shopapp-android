package com.shopify.router

import android.app.Activity
import com.domain.router.ExternalRouter
import com.shopify.ui.checkout.CheckoutActivity

class ShopifyRouter : ExternalRouter {

    override fun openCheckoutActivity(activity: Activity) {
        activity.startActivity(CheckoutActivity.getStartIntent(activity))
    }
}