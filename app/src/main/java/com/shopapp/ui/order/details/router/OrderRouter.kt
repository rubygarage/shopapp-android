package com.shopapp.ui.order.details.router

import android.content.Context
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.checkout.CheckoutActivity
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.product.ProductDetailsActivity

class OrderRouter {

    fun showProduct(context: Context?, variant: ProductVariant) {
        context?.let { it.startActivity(ProductDetailsActivity.getStartIntent(it, variant)) }
    }

}