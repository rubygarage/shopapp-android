package com.shopapp.ui.cart.router

import android.content.Context
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.checkout.CheckoutActivity
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.product.ProductDetailsActivity

class CartRouter {

    fun showCheckout(context: Context?) {
        context?.let { it.startActivity(CheckoutActivity.getStartIntent(it)) }
    }

    fun showHome(context: Context?, isNewTask: Boolean = false) {
        context?.let { it.startActivity(HomeActivity.getStartIntent(it, isNewTask)) }
    }

    fun showProduct(context: Context?, variant: ProductVariant) {
        context?.let { it.startActivity(ProductDetailsActivity.getStartIntent(it, variant)) }
    }

}