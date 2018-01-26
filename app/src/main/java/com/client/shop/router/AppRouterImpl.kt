package com.client.shop.router

import android.content.Context
import android.support.v4.app.TaskStackBuilder
import com.client.shop.ui.home.HomeActivity
import com.client.shop.ui.order.success.OrderSuccessActivity
import com.client.shop.ui.product.ProductDetailsActivity
import com.domain.entity.ProductVariant
import com.domain.router.AppRouter

class AppRouterImpl : AppRouter {

    override fun openProductDetailsScreen(context: Context, productVariant: ProductVariant) {
        context.startActivity(ProductDetailsActivity.getStartIntent(context, productVariant))
    }

    override fun openOrderSuccessScreen(context: Context, orderId: String, orderNumber: Int) {
        val taskBuilder = TaskStackBuilder.create(context)
        taskBuilder.addNextIntent(HomeActivity.getStartIntent(context, true))
        taskBuilder.addNextIntent(OrderSuccessActivity.getStartIntent(context, orderId, orderNumber))
        taskBuilder.startActivities()
    }
}