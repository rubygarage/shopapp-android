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
                processedAt = orderAdaptee.processedAt.toDate(),
                variants = orderAdaptee.lineItems.edges
                        .map { it.node.variant }
                        .filter { it != null }
                        .map { ProductVariantAdapter.adapt(it) },
                paginationValue = paginationValue
        )
    }

}
