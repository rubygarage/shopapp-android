package com.client.shop.ext

import android.text.TextUtils
import android.util.Patterns

fun String.isEmailValid(): Boolean =
        !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.isPasswordValid(): Boolean = !TextUtils.isEmpty(this) && this.length >= 6

fun String.isCardValid(number: String): Boolean {

    if (number.isEmpty())
        return false

    var sum = 0
    var alternate = false
    for (i in number.length - 1 downTo 0) {
        var n = Integer.parseInt(number.substring(i, i + 1))
        if (alternate) {
            n *= 2
            if (n > 9) {
                n = n % 10 + 1
            }
        }
        sum += n
        alternate = !alternate
    }

    return sum % 10 == 0
}