package com.shopapp.data.dao

import com.shopapp.gateway.entity.CartProduct
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Dao {

    fun getCartDataList(): Observable<List<CartProduct>>

    fun addCartProduct(cartProduct: CartProduct): Single<CartProduct>

    fun deleteProductFromCart(productVariantId: String): Completable

    fun deleteProductListFromCart(productVariantListId: List<String>): Completable

    fun deleteAllProductsFromCart(): Completable

    fun changeCartProductQuantity(productVariantId: String, newQuantity: Int): Single<CartProduct>
}