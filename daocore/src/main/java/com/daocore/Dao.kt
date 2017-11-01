package com.daocore

import com.domain.entity.CartItem
import com.domain.entity.ProductVariant
import io.reactivex.Flowable

interface Dao {

    fun getCartItems(): Flowable<CartItem>

    fun addProductToCart(productVariant: ProductVariant, quantity: Int)

    fun deleteProductFromCart(productVariantId: String)

    fun changeCartProductQuantity(productVariantId: String, newQuantity: Int)
}