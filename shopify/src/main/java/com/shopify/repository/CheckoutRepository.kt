package com.shopify.repository

import com.domain.entity.*
import com.google.android.gms.wallet.FullWallet
import com.shopify.buy3.pay.PayCart
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
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

    fun createPayCart(checkout: Checkout, cartProductList: List<CartProduct>): Single<PayCart>

    fun completeCheckoutByAndroidPay(checkout: Checkout, payCart: PayCart,
                                     fullWallet: FullWallet): Single<Boolean>
}