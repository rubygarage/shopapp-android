package com.shopify.repository

import com.domain.entity.Card
import com.domain.entity.CartProduct
import com.shopify.entity.Checkout
import io.reactivex.Single

interface CheckoutRepository {

    fun createCheckout(cartProductList: List<CartProduct>): Single<Checkout>

    fun payByCard(card: Card): Single<String>

    fun completeCheckoutByCard(checkoutId: String, creditCardVaultToken: String): Single<Boolean>
}