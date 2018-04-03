package com.shopapp.ui.cart.router

import android.content.Context
import com.shopapp.ui.cart.CartActivity

class CartWidgetRouter {

    fun showCart(context: Context?) {
        context?.let { it.startActivity(CartActivity.getStartIntent(it)) }
    }

}