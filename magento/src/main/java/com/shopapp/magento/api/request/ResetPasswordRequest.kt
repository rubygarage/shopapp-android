package com.shopapp.magento.api.request

class ResetPasswordRequest(val email: String, val template: String = TEMPLATE) {

    companion object {
        private const val TEMPLATE = "email_reset"
    }
}