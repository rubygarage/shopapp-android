package com.domain.entity

import java.util.*

data class Order(
        var id: String,
        var currency: String,
        val email: String,
        val orderNumber: Int,
        val totalPrice: Double,
        var processedAt: Date,
        var variants: List<ProductVariant>,
        var paginationValue: String? = null
)