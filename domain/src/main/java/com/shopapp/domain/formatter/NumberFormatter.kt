package com.shopapp.domain.formatter

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*

class NumberFormatter {

    fun formatPrice(value: BigDecimal, currencyCode: String): String {
        return formatPriceValue(value, currencyCode)
    }

    private fun formatPriceValue(value: Any, currencyCode: String): String {
        val formatter = DecimalFormat.getCurrencyInstance()
        formatter.currency = Currency.getInstance(currencyCode)
        return formatter.format(value)
    }
}