package com.shopify.constant

import android.support.annotation.StringDef

@StringDef(WEB_PAYMENT, CARD_PAYMENT, ANDROID_PAYMENT)
@Retention(AnnotationRetention.SOURCE)
annotation class PaymentType

const val WEB_PAYMENT = "web_payment"
const val CARD_PAYMENT = "card_payment"
const val ANDROID_PAYMENT = "android_payment"