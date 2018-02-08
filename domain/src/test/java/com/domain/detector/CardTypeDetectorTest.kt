package com.domain.detector

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@Suppress("FunctionName")
@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class CardTypeDetectorTest {

    private val cardTypeDetector = CardTypeDetector()

    @Test
    fun cardTypeDetector_Visa_ReturnsCardType() {
        Assert.assertEquals(CardTypeDetector.CardType.VISA,
            cardTypeDetector.detect("4242424242424242"))
        Assert.assertEquals(CardTypeDetector.CardType.VISA,
            cardTypeDetector.detect("4111111111111111"))
    }

    @Test
    fun cardTypeDetector_MasterCard_ReturnsCardType() {
        Assert.assertEquals(CardTypeDetector.CardType.MASTER_CARD,
            cardTypeDetector.detect("5105105105105100"))
        Assert.assertEquals(CardTypeDetector.CardType.MASTER_CARD,
            cardTypeDetector.detect("5200828282828210"))
        Assert.assertEquals(CardTypeDetector.CardType.MASTER_CARD,
            cardTypeDetector.detect("5555555555554444"))
    }

    @Test
    fun cardTypeDetector_AmericanExpress_ReturnsCardType() {
        Assert.assertEquals(CardTypeDetector.CardType.AMERICAN_EXPRESS,
            cardTypeDetector.detect("371449635398431"))
        Assert.assertEquals(CardTypeDetector.CardType.AMERICAN_EXPRESS,
            cardTypeDetector.detect("378282246310005"))
        Assert.assertEquals(CardTypeDetector.CardType.AMERICAN_EXPRESS,
            cardTypeDetector.detect("378734493671000"))
    }

    @Test
    fun cardTypeDetector_Discover_ReturnsCardType() {
        Assert.assertEquals(CardTypeDetector.CardType.DISCOVER,
            cardTypeDetector.detect("6011111111111117"))
        Assert.assertEquals(CardTypeDetector.CardType.DISCOVER,
            cardTypeDetector.detect("6011000990139424"))
    }

    @Test
    fun cardTypeDetector_DinersClub_ReturnsCardType() {
        Assert.assertEquals(CardTypeDetector.CardType.DINERS_CLUB,
            cardTypeDetector.detect("30569309025904"))
        Assert.assertEquals(CardTypeDetector.CardType.DINERS_CLUB,
            cardTypeDetector.detect("38520000023237"))
    }

    @Test
    fun cardTypeDetector_JCB_ReturnsCardType() {
        Assert.assertEquals(CardTypeDetector.CardType.JCB,
            cardTypeDetector.detect("3530111333300000"))
        Assert.assertEquals(CardTypeDetector.CardType.JCB,
            cardTypeDetector.detect("3566002020360505"))
    }

    @Test
    fun cardTypeDetector_Unknown_ReturnsNull() {
        Assert.assertNull(cardTypeDetector.detect("5019717010103742"))
        Assert.assertNull(cardTypeDetector.detect("6331101999990016"))
    }

}