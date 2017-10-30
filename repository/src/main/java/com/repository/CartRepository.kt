package com.repository

import com.domain.entity.Cart
import com.domain.entity.ProductVariant
import io.reactivex.Single

interface CartRepository {

    fun getCart(): Single<Cart>

    fun addProductToCart(product: ProductVariant, quantity: Int): Single<Cart>

    fun removeProductFromCart(productId: String): Single<Cart>

    fun updateProductQuantity(productId: String, newQuantity: Int): Single<Cart>
}