package com.shopapp.gateway.entity

import java.math.BigDecimal
import java.util.*

data class Order(
    val id: String,
    val currency: String,
    val email: String,
    val orderNumber: Int,
    val subtotalPrice: BigDecimal?,
    val totalShippingPrice: BigDecimal?,
    val totalPrice: BigDecimal,
    val processedAt: Date,
    val orderProducts: List<OrderProduct>,
    val address: Address?,
    val paginationValue: String? = null
)