package com.domain.detector

import com.domain.R

class CardTypeDetector {

    companion object {
        private val VISA_PATTERN = "^4[0-9]{12}(?:[0-9]{3}){0,2}$".toRegex()
        private val MASTERCARD_PATTERN = "^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$".toRegex()
        private val AMERICAN_EXPRESS_PATTERN = "^3[47][0-9]{13}$".toRegex()
        private val DINERS_CLUB_PATTERN = "3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})\\d{10}$".toRegex()
        private val DISCOVER_PATTERN = "^6(?:011|[45][0-9]{2})[0-9]{12}$".toRegex()
        private val JCB_PATTERN = "^(?:2131|1800|35\\d{3})\\d{11}$".toRegex()
    }

    enum class CardType(val nameRes: Int, val logoRes: Int) {
        VISA(R.string.visa, R.drawable.ic_visa),
        MASTER_CARD(R.string.mastercard, R.drawable.ic_master_card),
        AMERICAN_EXPRESS(R.string.american_express, R.drawable.ic_amex),
        DINERS_CLUB(R.string.diners_club, R.drawable.ic_dc_card),
        DISCOVER(R.string.discover, R.drawable.ic_discover),
        JCB(R.string.jcb, R.drawable.ic_jcb_card)
    }

    fun detect(cardNumber: String): CardType? {
        return when {
            VISA_PATTERN.matches(cardNumber) -> CardType.VISA
            MASTERCARD_PATTERN.matches(cardNumber) -> CardType.MASTER_CARD
            AMERICAN_EXPRESS_PATTERN.matches(cardNumber) -> CardType.AMERICAN_EXPRESS
            DINERS_CLUB_PATTERN.matches(cardNumber) -> CardType.DINERS_CLUB
            DISCOVER_PATTERN.matches(cardNumber) -> CardType.DISCOVER
            JCB_PATTERN.matches(cardNumber) -> CardType.JCB
            else -> null
        }
    }
}