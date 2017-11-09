package com.daocore

import com.domain.entity.CartProduct
import com.domain.entity.Customer
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface Dao {

    fun getCartDataList(): Observable<List<CartProduct>>

    fun addCartProduct(cartProduct: CartProduct): Single<CartProduct>

    fun deleteProductFromCart(productVariantId: String): Completable

    fun changeCartProductQuantity(productVariantId: String, newQuantity: Int): Single<CartProduct>

    fun saveCustomer(customer: Customer): Single<Customer>

    fun isLoggedIn(): Single<Boolean>
}