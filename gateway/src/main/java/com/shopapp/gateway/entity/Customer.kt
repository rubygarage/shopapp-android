package com.shopapp.gateway.entity

data class Customer(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    var phone: String,
    var isAcceptsMarketing: Boolean,
    val addressList: List<Address>,
    val defaultAddress: Address?
)