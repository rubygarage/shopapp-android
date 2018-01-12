package com.domain.detector

import android.content.Context
import com.domain.R
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config


@Suppress("FunctionName")
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CardTypeDetectorTest {

    private val cardTypeDetector = CardTypeDetector()
    private val context: Context = RuntimeEnvironment.application

    @Test
    fun cardTypeDetector_Visa_ReturnsString() {
        Assert.assertEquals(context.getString(R.string.visa),
                cardTypeDetector.detect("4242424242424242", context))
        Assert.assertEquals(context.getString(R.string.visa),
                cardTypeDetector.detect("4111111111111111", context))
    }

    @Test
    fun cardTypeDetector_MasterCard_ReturnsString() {
        Assert.assertEquals(context.getString(R.string.mastercard),
                cardTypeDetector.detect("5105105105105100", context))
        Assert.assertEquals(context.getString(R.string.mastercard),
                cardTypeDetector.detect("5200828282828210", context))
        Assert.assertEquals(context.getString(R.string.mastercard),
                cardTypeDetector.detect("5555555555554444", context))
    }

    @Test
    fun cardTypeDetector_AmericanExpress_ReturnsString() {
        Assert.assertEquals(context.getString(R.string.american_express),
                cardTypeDetector.detect("371449635398431", context))
        Assert.assertEquals(context.getString(R.string.american_express),
                cardTypeDetector.detect("378282246310005", context))
        Assert.assertEquals(context.getString(R.string.american_express),
                cardTypeDetector.detect("378734493671000", context))
    }

    @Test
    fun cardTypeDetector_Discover_ReturnsString() {
        Assert.assertEquals(context.getString(R.string.discover),
                cardTypeDetector.detect("6011111111111117", context))
        Assert.assertEquals(context.getString(R.string.discover),
                cardTypeDetector.detect("6011000990139424", context))
    }

    @Test
    fun cardTypeDetector_DinersClub_ReturnsString() {
        Assert.assertEquals(context.getString(R.string.diners_club),
                cardTypeDetector.detect("30569309025904", context))
        Assert.assertEquals(context.getString(R.string.diners_club),
                cardTypeDetector.detect("38520000023237", context))
    }

    @Test
    fun cardTypeDetector_JCB_ReturnsString() {
        Assert.assertEquals(context.getString(R.string.jcb),
                cardTypeDetector.detect("3530111333300000", context))
        Assert.assertEquals(context.getString(R.string.jcb),
                cardTypeDetector.detect("3566002020360505", context))
    }

    @Test
    fun cardTypeDetector_Unknown_ReturnsNull() {
        Assert.assertNull(cardTypeDetector.detect("5019717010103742", context))
        Assert.assertNull(cardTypeDetector.detect("6331101999990016", context))
    }

}