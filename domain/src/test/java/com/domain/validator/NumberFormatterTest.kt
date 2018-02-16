package com.domain.validator

import com.domain.formatter.NumberFormatter
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.util.*

@Suppress("FunctionName")
class NumberFormatterTest {

    private val formatter = NumberFormatter()
    private val CURRENCY = "USD"

    @Test
    fun numberFormatter_FormatBigDecimalPrice_US_Equals() {

        Locale.setDefault(Locale.US)

        assertEquals("$10.22", formatter.formatPrice(BigDecimal.valueOf(10.22), CURRENCY))
        assertEquals("$10.22", formatter.formatPrice(BigDecimal.valueOf(10.222), CURRENCY))
        assertEquals("$10.22", formatter.formatPrice(BigDecimal.valueOf(10.2222222), CURRENCY))

        assertEquals("($76.12)", formatter.formatPrice(BigDecimal.valueOf(-76.12), CURRENCY))
        assertEquals("($76.12)", formatter.formatPrice(BigDecimal.valueOf(-76.12222), CURRENCY))

        assertEquals("$1.00", formatter.formatPrice(BigDecimal.valueOf(1.00000), CURRENCY))
        assertEquals("$1.00", formatter.formatPrice(BigDecimal.valueOf(1.0), CURRENCY))
        assertEquals("$1.00", formatter.formatPrice(BigDecimal.valueOf(1), CURRENCY))
    }

    @Test
    fun numberFormatter_FormatBigDecimalPrice_UK_Equals() {

        Locale.setDefault(Locale.UK)

        assertEquals("USD10.22", formatter.formatPrice(BigDecimal.valueOf(10.22), CURRENCY))
        assertEquals("USD10.22", formatter.formatPrice(BigDecimal.valueOf(10.222), CURRENCY))
        assertEquals("USD10.22", formatter.formatPrice(BigDecimal.valueOf(10.2222222), CURRENCY))

        assertEquals("-USD76.12", formatter.formatPrice(BigDecimal.valueOf(-76.12), CURRENCY))
        assertEquals("-USD76.12", formatter.formatPrice(BigDecimal.valueOf(-76.12222), CURRENCY))

        assertEquals("USD1.00", formatter.formatPrice(BigDecimal.valueOf(1.00000), CURRENCY))
        assertEquals("USD1.00", formatter.formatPrice(BigDecimal.valueOf(1.0), CURRENCY))
        assertEquals("USD1.00", formatter.formatPrice(BigDecimal.valueOf(1), CURRENCY))
    }
}