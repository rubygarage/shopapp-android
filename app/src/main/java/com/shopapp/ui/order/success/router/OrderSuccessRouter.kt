package com.shopapp.ui.order.success.router

import android.content.Context
import com.shopapp.ui.order.details.OrderDetailsActivity

class OrderSuccessRouter {

    fun showOrder(context: Context?, id: String) {
        context?.let { it.startActivity(OrderDetailsActivity.getStartIntent(it, id)) }
    }

}