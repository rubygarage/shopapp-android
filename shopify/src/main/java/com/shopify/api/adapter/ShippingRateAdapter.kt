package com.shopify.api.adapter

import com.shopify.buy3.Storefront
import com.shopify.entity.ShippingRate

object ShippingRateAdapter {

    fun adapt(adaptee: Storefront.ShippingRate): ShippingRate {
        return ShippingRate(
                adaptee.title,
                adaptee.price,
                adaptee.handle
        )
    }
}