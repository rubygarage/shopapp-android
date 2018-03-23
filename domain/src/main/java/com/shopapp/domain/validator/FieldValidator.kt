package com.shopapp.domain.validator

import com.shopapp.gateway.entity.Address
import java.util.regex.Pattern

class FieldValidator {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }

    private val EMAIL_ADDRESS = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}"
                    + "\\@"
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}"
                    + "("
                    + "\\."
                    + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}"
                    + ")+"
    )

    fun isEmailValid(email: String): Boolean = EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordValid(password: String): Boolean = !password.isEmpty()
            && password.length >= MIN_PASSWORD_LENGTH

    fun isAddressValid(addressObject: Address): Boolean {
        return with(addressObject) {
            address.isNotBlank()
                    && city.isNotBlank()
                    && country.isNotBlank()
                    && firstName.isNotBlank()
                    && lastName.isNotBlank()
                    && zip.isNotBlank()
        }
    }
}