package com.shopapp.magento.api.request

class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String
)