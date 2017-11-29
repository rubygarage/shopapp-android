package com.domain.formatter

import java.math.BigDecimal
import java.text.DecimalFormat

class NumberFormatter {

    fun formatPrice(value: Float): String {
        return formatPriceValue(value)
    }

    fun formatPrice(value: BigDecimal): String {
        return formatPriceValue(value)
    }

    private fun formatPriceValue(value: Any): String {
        val formatter = DecimalFormat.getCurrencyInstance() as DecimalFormat
        val symbols = formatter.decimalFormatSymbols
        symbols.currencySymbol = ""
        symbols.internationalCurrencySymbol = ""
        formatter.decimalFormatSymbols = symbols
        formatter.negativePrefix = "-"
        formatter.positiveSuffix = ""
        formatter.negativeSuffix = ""
        return formatter.format(value)
    }
}