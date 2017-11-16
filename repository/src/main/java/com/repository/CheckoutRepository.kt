package com.repository

import com.domain.entity.CartProduct
import io.reactivex.Single

interface CheckoutRepository {

    fun createCheckout(cartProductList: List<CartProduct>): Single<Pair<String, String>>
}