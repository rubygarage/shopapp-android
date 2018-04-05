package com.shopapp.ui.order.list.router

import android.content.Context
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.home.HomeActivity
import com.shopapp.ui.order.details.OrderDetailsActivity
import com.shopapp.ui.product.ProductDetailsActivity

class OrderListRouter {

    fun showProduct(context: Context?, variant: ProductVariant) {
        context?.let { it.startActivity(ProductDetailsActivity.getStartIntent(it, variant)) }
    }

    fun showHome(context: Context?, isNewTask: Boolean = false) {
        context?.let { it.startActivity(HomeActivity.getStartIntent(it, isNewTask)) }
    }

    fun showOrder(context: Context?, id: String) {
        context?.let { it.startActivity(OrderDetailsActivity.getStartIntent(it, id)) }
    }
}