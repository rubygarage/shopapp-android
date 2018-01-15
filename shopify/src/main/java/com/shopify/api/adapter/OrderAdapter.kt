package com.shopify.api.adapter

import com.domain.entity.Order
import com.shopify.buy3.Storefront

object OrderAdapter {

    fun adapt(orderAdaptee: Storefront.Order, paginationValue: String? = null): Order {
        return Order(
                id = orderAdaptee.id.toString(),
                currency = orderAdaptee.currencyCode.toString(),
                email = orderAdaptee.email,
                orderNumber = orderAdaptee.orderNumber,
                totalPrice = orderAdaptee.totalPrice.toDouble(),
                subtotalPrice = orderAdaptee.subtotalPrice.toDouble(),
                totalShippingPrice = orderAdaptee.totalShippingPrice.toDouble(),
                address = orderAdaptee.shippingAddress?.let {
                    AddressAdapter.adapt(it)
                },
                processedAt = orderAdaptee.processedAt.toDate(),
                orderProducts = orderAdaptee.lineItems.edges
                        .map { it.node }
                        .filter { it != null }
                        .map { OrderProductAdapter.adapt(it) },
                paginationValue = paginationValue
        )
    }

}
