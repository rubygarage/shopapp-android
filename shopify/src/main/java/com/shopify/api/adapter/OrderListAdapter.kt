package com.shopify.api.adapter

import com.domain.entity.Order
import com.shopify.buy3.Storefront

object OrderListAdapter {

    fun adapt(orderConnection: Storefront.OrderConnection?): List<Order> =
            orderConnection?.edges?.map { OrderAdapter.adapt(it.node, it.cursor) } ?: listOf()
}
