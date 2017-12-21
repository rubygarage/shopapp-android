package com.domain.entity

data class Address(
        val address: String,
        val secondAddress: String,
        val city: String,
        val country: String,
        val state: String?,
        val firstName: String,
        val lastName: String,
        val zip: String,
        val phone: String?
)