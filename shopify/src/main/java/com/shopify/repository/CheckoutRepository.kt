package com.shopify.repository

import com.domain.entity.Address
import com.domain.entity.Card
import com.domain.entity.CartProduct
import com.google.android.gms.wallet.FullWallet
import com.shopify.buy3.pay.PayCart
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
import io.reactivex.Completable
import io.reactivex.Single

interface CheckoutRepository {

    fun createCheckout(cartProductList: List<CartProduct>): Single<Checkout>

    fun getCheckout(checkoutId: String): Single<Checkout>

    fun setShippingAddress(checkoutId: String, address: Address): Completable

    fun getShippingRates(checkoutId: String, email: String, address: Address): Single<List<ShippingRate>>

    fun selectShippingRate(checkoutId: String, shippingRate: ShippingRate): Single<Checkout>

    fun payByCard(card: Card): Single<String>

    fun completeCheckoutByCard(checkout: Checkout, address: Address, creditCardVaultToken: String): Single<Boolean>

    fun createPayCart(checkout: Checkout, cartProductList: List<CartProduct>): Single<PayCart>

    fun completeCheckoutByAndroidPay(checkout: Checkout, payCart: PayCart,
                                     fullWallet: FullWallet): Single<Boolean>
}