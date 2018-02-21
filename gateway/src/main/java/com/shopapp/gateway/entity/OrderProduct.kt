package com.client.shop.gateway.entity

data class OrderProduct(
    val title: String,
    val productVariant: ProductVariant,
    val quantity: Int
)