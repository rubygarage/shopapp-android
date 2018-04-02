package com.shopapp.gateway.entity

data class OrderProduct(
    val title: String,
    val productVariant: ProductVariant?,
    val quantity: Int
)