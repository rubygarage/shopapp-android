package com.shopapp.domain.validator

import com.shopapp.gateway.entity.Card
import java.util.*

class CardValidator {

    fun splitHolderName(holderName: String): Pair<String, String>? =
        split(holderName, "\\s".toRegex())

    fun splitExpireDate(expireDate: String): Pair<String, String>? =
        split(expireDate, "/".toRegex())

    fun isCardValid(card: Card): CardValidationResult {

        with(card) {
            if (firstName.isBlank() || lastName.isBlank()) {
                return CardValidationResult.INVALID_NAME
            } else if (!isDateValid(expireMonth, expireYear)) {
                return CardValidationResult.INVALID_DATE
            } else if (verificationCode.isBlank() || verificationCode.length !in 3..4) {
                return CardValidationResult.INVALID_CVV
            } else if (!isNumberValid(cardNumber)) {
                return CardValidationResult.INVALID_NUMBER
            }
        }
        return CardValidationResult.VALID
    }

    private fun split(originalString: String, regex: Regex): Pair<String, String>? {
        val result = originalString.split(regex, 2)
        return if (result.size == 2) {
            Pair(result[0], result[1])
        } else {
            null
        }
    }

    private fun isDateValid(expireMonth: String, expireYear: String): Boolean {

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentYear = calendar.get(Calendar.YEAR)

        val incorrectMonth = expireMonth.toIntOrNull() ?: -1
        val selectedMonth = incorrectMonth - 1
        val selectedYear = expireYear.toIntOrNull() ?: -1

        return when {
            selectedMonth > Calendar.DECEMBER || selectedMonth < 0 -> false
            currentYear > selectedYear -> false
            else -> !(currentMonth > selectedMonth && currentYear == selectedYear)
        }
    }

    private fun isNumberValid(number: String): Boolean {

        if (number.isNotBlank()) {
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
        } else {
            return false
        }
    }

    enum class CardValidationResult {
        VALID,
        INVALID_DATE,
        INVALID_NAME,
        INVALID_NUMBER,
        INVALID_CVV
    }
}