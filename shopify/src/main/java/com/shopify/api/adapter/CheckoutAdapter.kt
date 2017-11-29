package com.shopify.api.adapter

import com.shopify.buy3.Storefront
import com.shopify.entity.Checkout

object CheckoutAdapter {

    fun adapt(adaptee: Storefront.Checkout, currency: String): Checkout {
        return Checkout(
                adaptee.id.toString(),
                adaptee.webUrl,
                adaptee.requiresShipping,
                adaptee.subtotalPrice,
                adaptee.totalPrice,
                adaptee.totalTax,
                currency
        )
    }
}