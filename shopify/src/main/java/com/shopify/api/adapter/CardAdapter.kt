package com.shopify.api.adapter

import com.client.shop.gateway.entity.CardType
import com.shopify.buy3.Storefront

object CardAdapter {

    fun adapt(adaptee: List<Storefront.CardBrand>): List<CardType> {
        return adaptee.map { adapt(it) }.mapNotNull { it }
    }

    fun adapt(adaptee: Storefront.CardBrand): CardType? {
        return when (adaptee) {
            Storefront.CardBrand.MASTERCARD -> CardType.MASTER_CARD
            Storefront.CardBrand.VISA -> CardType.VISA
            Storefront.CardBrand.AMERICAN_EXPRESS -> CardType.AMERICAN_EXPRESS
            Storefront.CardBrand.DINERS_CLUB -> CardType.DINERS_CLUB
            Storefront.CardBrand.DISCOVER -> CardType.DISCOVER
            Storefront.CardBrand.JCB -> CardType.JCB
            else -> null
        }
    }
}