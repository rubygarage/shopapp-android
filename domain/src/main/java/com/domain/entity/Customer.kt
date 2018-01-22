package com.domain.entity

data class Customer(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    var phone: String,
    val addressList: List<Address>,
    val defaultAddress: Address?
)