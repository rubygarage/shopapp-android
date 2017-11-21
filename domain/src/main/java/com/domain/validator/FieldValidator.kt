package com.domain.validator

import android.text.TextUtils
import android.util.Patterns

class FieldValidator {

    fun isEmailValid(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    fun isPasswordValid(password: String): Boolean = !TextUtils.isEmpty(password) && password.length >= 6
}