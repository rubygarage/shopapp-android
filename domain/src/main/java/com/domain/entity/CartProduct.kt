package com.domain.entity

data class CartProduct(
    val productVariant: ProductVariant,
    val productId: String,
    val currency: String,
    val quantity: Int
)