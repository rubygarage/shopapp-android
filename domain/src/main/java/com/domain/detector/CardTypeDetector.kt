package com.domain.detector

import android.content.Context
import com.domain.R

class CardTypeDetector {

    companion object {
        private val VISA_PATTERN = "^4[0-9]{12}(?:[0-9]{3}){0,2}$".toRegex()
        private val MASTERCARD_PATTERN = "^(?:5[1-5]|2(?!2([01]|20)|7(2[1-9]|3))[2-7])\\d{14}$".toRegex()
        private val AMERICAN_EXPRESS_PATTERN = "^3[47][0-9]{13}$".toRegex()
        private val DINERS_CLUB_PATTERN = "3(?:0[0-5]\\d|095|6\\d{0,2}|[89]\\d{2})\\d{10}$".toRegex()
        private val DISCOVER_PATTERN = "^6(?:011|[45][0-9]{2})[0-9]{12}$".toRegex()
        private val JCB_PATTERN = "^(?:2131|1800|35\\d{3})\\d{11}$".toRegex()
        private val CHINA_UNION_PAY_PATTERN = "^62[0-9]{14,17}$".toRegex()
    }

    fun detect(cardNumber: String, context: Context): String? {
        return when {
            VISA_PATTERN.matches(cardNumber) -> context.getString(R.string.visa)
            MASTERCARD_PATTERN.matches(cardNumber) -> context.getString(R.string.mastercard)
            AMERICAN_EXPRESS_PATTERN.matches(cardNumber) -> context.getString(R.string.american_express)
            DINERS_CLUB_PATTERN.matches(cardNumber) -> context.getString(R.string.diners_club)
            DISCOVER_PATTERN.matches(cardNumber) -> context.getString(R.string.discover)
            JCB_PATTERN.matches(cardNumber) -> context.getString(R.string.jcb)
            CHINA_UNION_PAY_PATTERN.matches(cardNumber) -> context.getString(R.string.china_union_pay)
            else -> null
        }
    }
}