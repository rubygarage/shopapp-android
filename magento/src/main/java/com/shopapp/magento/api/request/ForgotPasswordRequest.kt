package com.shopapp.magento.api.request

class ForgotPasswordRequest(val email: String, val template: String = TEMPLATE) {

    companion object {
        private const val TEMPLATE = "email_reset"
    }
}