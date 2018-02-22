package com.shopapp.gateway.entity

import com.shopapp.gateway.R


enum class CardType(val nameRes: Int, val logoRes: Int) {
    VISA(R.string.visa, R.drawable.ic_visa),
    MASTER_CARD(R.string.mastercard, R.drawable.ic_master_card),
    AMERICAN_EXPRESS(R.string.american_express, R.drawable.ic_amex),
    DINERS_CLUB(R.string.diners_club, R.drawable.ic_dc_card),
    DISCOVER(R.string.discover, R.drawable.ic_discover),
    JCB(R.string.jcb, R.drawable.ic_jcb_card)
}