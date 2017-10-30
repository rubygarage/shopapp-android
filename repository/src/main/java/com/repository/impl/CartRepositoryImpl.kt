package com.repository.impl

import com.apicore.Api
import com.domain.entity.Cart
import com.domain.entity.ProductVariant
import com.repository.CartRepository
import io.reactivex.Single

class CartRepositoryImpl(private val api: Api) : CartRepository {

    override fun getCart(): Single<Cart> {
        return Single.just(null)
    }

    override fun addProductToCart(product: ProductVariant, quantity: Int): Single<Cart> {
        return Single.just(null)
    }

    override fun removeProductFromCart(productId: String): Single<Cart> {
        return Single.just(null)
    }

    override fun updateProductQuantity(productId: String, newQuantity: Int): Single<Cart> {
        return Single.just(null)
    }
}