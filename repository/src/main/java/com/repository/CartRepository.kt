package com.repository

import com.domain.entity.CartProduct
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface CartRepository {

    fun getCartProductList(): Observable<List<CartProduct>>

    fun addCartProduct(cartProduct: CartProduct): Single<CartProduct>

    fun deleteProductFromCart(productVariantId: String): Completable?

    fun changeCartProductQuantity(productVariantId: String, newQuantity: Int): Single<CartProduct>?
}