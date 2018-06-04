package com.shopapp.magento.api.request

class UpdatePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)