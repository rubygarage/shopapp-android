package com.shopify.api.adapter

import com.domain.entity.OrderProduct
import com.shopify.buy3.Storefront

object OrderProductAdapter {

    fun adapt(orderAdaptee: Storefront.OrderLineItem): OrderProduct {
        return OrderProduct(
                title = orderAdaptee.title,
                productVariant = ProductVariantAdapter.adapt(orderAdaptee.variant),
                quantity = orderAdaptee.quantity
        )
    }

}
