package com.repository

import com.domain.entity.CartItem
import com.domain.entity.ProductVariant
import io.reactivex.Flowable

interface CartRepository {

    fun getCartItems(): Flowable<CartItem>

    fun addProductToCart(productVariant: ProductVariant, quantity: Int)

    fun deleteProductFromCart(productVariantId: String)

    fun changeCartProductQuantity(productVariantId: String, newQuantity: Int)
}