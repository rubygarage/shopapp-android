package com.shopify.entity

import com.domain.entity.Address
import java.math.BigDecimal

data class Checkout(
        val checkoutId: String,
        val webUrl: String,
        val requiresShipping: Boolean,
        val subtotalPrice: BigDecimal,
        val totalPrice: BigDecimal,
        val taxPrice: BigDecimal,
        val currency: String,
        val address: Address?,
        val shippingRate: ShippingRate?
)