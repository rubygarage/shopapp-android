package com.shopapp.gateway.entity

data class Customer(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val isAcceptsMarketing: Boolean,
    val addressList: List<Address>,
    val defaultAddress: Address?
)