package com.domain.entity

data class AccessData(
        val email: String,
        val accessToken: String,
        val expiresAt: Long
)