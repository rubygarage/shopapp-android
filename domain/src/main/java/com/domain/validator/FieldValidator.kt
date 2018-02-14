package com.domain.validator

import android.text.TextUtils
import android.util.Patterns
import com.client.shop.getaway.entity.Address

class FieldValidator {

    companion object {
        private const val MIN_PASSWORD_LENGTH = 8
    }

    fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordValid(password: String): Boolean = !TextUtils.isEmpty(password)
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