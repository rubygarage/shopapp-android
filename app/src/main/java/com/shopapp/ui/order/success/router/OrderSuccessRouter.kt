package com.shopapp.ui.order.success.router

import android.content.Context
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.checkout.CheckoutActivity
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.details.OrderDetailsActivity
import com.shopapp.ui.product.ProductDetailsActivity

class OrderSuccessRouter {

    fun showOrder(context: Context?, id: String) {
        context?.let { it.startActivity(OrderDetailsActivity.getStartIntent(it, id)) }
    }

}