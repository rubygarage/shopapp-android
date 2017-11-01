package com.repository.impl

import com.apicore.Api
import com.daocore.Dao
import com.domain.entity.CartItem
import com.domain.entity.ProductVariant
import com.repository.CartRepository
import io.reactivex.Flowable

class CartRepositoryImpl(private val api: Api, private val dao: Dao) : CartRepository {

    override fun getCartItems(): Flowable<CartItem> {
        return dao.getCartItems()
    }

    override fun addProductToCart(productVariant: ProductVariant, quantity: Int) {
        dao.addProductToCart(productVariant, quantity)
    }

    override fun deleteProductFromCart(productVariantId: String) {
        dao.deleteProductFromCart(productVariantId)
    }

    override fun changeCartProductQuantity(productVariantId: String, newQuantity: Int) {
        dao.changeCartProductQuantity(productVariantId, newQuantity)
    }

}