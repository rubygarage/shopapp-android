package com.repository.impl

import com.apicore.Api
import com.daocore.Dao
import com.domain.entity.CartProduct
import com.repository.CartRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class CartRepositoryImpl(private val api: Api, private val dao: Dao) : CartRepository {

    override fun getCartProductList(): Observable<List<CartProduct>> {
        return dao.getCartDataList()
    }

    override fun addCartProduct(cartProduct: CartProduct): Single<CartProduct> {
        return dao.addCartProduct(cartProduct)
    }

    override fun deleteProductFromCart(productVariantId: String): Completable? {
        return dao.deleteProductFromCart(productVariantId)
    }

    override fun changeCartProductQuantity(productVariantId: String, newQuantity: Int): Single<CartProduct>? {
        return dao.changeCartProductQuantity(productVariantId, newQuantity)
    }

}