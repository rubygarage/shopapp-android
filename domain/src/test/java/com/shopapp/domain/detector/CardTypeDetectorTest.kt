package com.shopapp.domain.detector

import com.shopapp.gateway.entity.CardType
import org.junit.Assert
import org.junit.Test

class CardTypeDetectorTest {

    private val cardTypeDetector = CardTypeDetector()

    @Test
    fun shouldReturnVisaCardType() {
        Assert.assertEquals(CardType.VISA,
            cardTypeDetector.detect("4242424242424242"))
        Assert.assertEquals(CardType.VISA,
            cardTypeDetector.detect("4111111111111111"))
    }

    @Test
    fun shouldReturnMasterCardCardType() {
        Assert.assertEquals(CardType.MASTER_CARD,
            cardTypeDetector.detect("5105105105105100"))
        Assert.assertEquals(CardType.MASTER_CARD,
            cardTypeDetector.detect("5200828282828210"))
        Assert.assertEquals(CardType.MASTER_CARD,
            cardTypeDetector.detect("5555555555554444"))
    }

    @Test
    fun shouldReturnAmericanExpressCardType() {
        Assert.assertEquals(CardType.AMERICAN_EXPRESS,
            cardTypeDetector.detect("371449635398431"))
        Assert.assertEquals(CardType.AMERICAN_EXPRESS,
            cardTypeDetector.detect("378282246310005"))
        Assert.assertEquals(CardType.AMERICAN_EXPRESS,
            cardTypeDetector.detect("378734493671000"))
    }

    @Test
    fun shouldReturnDiscoverCardType() {
        Assert.assertEquals(CardType.DISCOVER,
            cardTypeDetector.detect("6011111111111117"))
        Assert.assertEquals(CardType.DISCOVER,
            cardTypeDetector.detect("6011000990139424"))
    }

    @Test
    fun shouldReturnDinersClubCardType() {
        Assert.assertEquals(CardType.DINERS_CLUB,
            cardTypeDetector.detect("30569309025904"))
        Assert.assertEquals(CardType.DINERS_CLUB,
            cardTypeDetector.detect("38520000023237"))
    }

    @Test
    fun shouldReturnJcbCardType() {
        Assert.assertEquals(CardType.JCB,
            cardTypeDetector.detect("3530111333300000"))
        Assert.assertEquals(CardType.JCB,
            cardTypeDetector.detect("3566002020360505"))
    }

    @Test
    fun shouldReturnNull() {
        Assert.assertNull(cardTypeDetector.detect("5019717010103742"))
        Assert.assertNull(cardTypeDetector.detect("6331101999990016"))
    }

}