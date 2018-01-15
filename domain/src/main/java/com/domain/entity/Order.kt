package com.domain.entity

import java.util.*

data class Order(
        val id: String,
        val currency: String,
        val email: String,
        val orderNumber: Int,
        val subtotalPrice: Double?,
        val totalShippingPrice: Double?,
        val totalPrice: Double,
        val processedAt: Date,
        val orderProducts: List<OrderProduct>,
        val address: Address?,
        val paginationValue: String? = null
)