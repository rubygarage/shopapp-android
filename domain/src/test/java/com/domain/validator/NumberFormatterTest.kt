package com.domain.validator

import com.domain.formatter.NumberFormatter
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.math.BigDecimal

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class NumberFormatterTest {

    private val formatter = NumberFormatter()

    @Test
    fun numberFormatter_FormatFloatPrice_Equals() {
        assertEquals("10.22", formatter.formatPrice(10.22f))
        assertEquals("10.22", formatter.formatPrice(10.222f))
        assertEquals("10.22", formatter.formatPrice(10.2222222f))

        assertEquals("-76.12", formatter.formatPrice(-76.12f))
        assertEquals("-76.12", formatter.formatPrice(-76.12222f))

        assertEquals("1.00", formatter.formatPrice(1.00000f))
        assertEquals("1.00", formatter.formatPrice(1.0f))
        assertEquals("1.00", formatter.formatPrice(1f))
    }

    @Test
    fun numberFormatter_FormatBigDecimalPrice_Equals() {
        assertEquals("10.22", formatter.formatPrice(BigDecimal.valueOf(10.22)))
        assertEquals("10.22", formatter.formatPrice(BigDecimal.valueOf(10.222)))
        assertEquals("10.22", formatter.formatPrice(BigDecimal.valueOf(10.2222222)))

        assertEquals("-76.12", formatter.formatPrice(BigDecimal.valueOf(-76.12)))
        assertEquals("-76.12", formatter.formatPrice(BigDecimal.valueOf(-76.12222)))

        assertEquals("1.00", formatter.formatPrice(BigDecimal.valueOf(1.00000)))
        assertEquals("1.00", formatter.formatPrice(BigDecimal.valueOf(1.0)))
        assertEquals("1.00", formatter.formatPrice(BigDecimal.valueOf(1)))
    }
}