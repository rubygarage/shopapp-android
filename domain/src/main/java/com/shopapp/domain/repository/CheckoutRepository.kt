package com.shopapp.domain.repository

import com.shopapp.gateway.entity.*
import io.reactivex.Single

interface CheckoutRepository {

    fun createCheckout(cartProductList: List<CartProduct>): Single<Checkout>

    fun getCheckout(checkoutId: String): Single<Checkout>

    fun setShippingAddress(checkoutId: String, address: Address): Single<Checkout>

    fun getShippingRates(checkoutId: String): Single<List<ShippingRate>>

    fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate): Single<Checkout>

    fun getAcceptedCardTypes(): Single<List<CardType>>

    fun getCardToken(card: Card): Single<String>

    fun completeCheckoutByCard(checkout: Checkout, email: String, address: Address, creditCardVaultToken: String): Single<Order>
}