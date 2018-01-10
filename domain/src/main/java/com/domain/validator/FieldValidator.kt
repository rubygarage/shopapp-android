package com.domain.validator

import android.text.TextUtils
import android.util.Patterns
import com.domain.entity.Address

class FieldValidator {

    fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordValid(password: String): Boolean = !TextUtils.isEmpty(password) && password.length >= 8

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