package com.domain.entity

data class Cart(
        val id: String,
        val productVariant: List<CartItem>)