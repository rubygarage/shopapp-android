package com.shopapp.data.impl

import com.shopapp.data.dao.Dao
import com.shopapp.domain.repository.CartRepository
import com.shopapp.gateway.entity.CartProduct
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class CartRepositoryImpl(private val dao: Dao) : CartRepository {

    override fun getCartProductList(): Observable<List<CartProduct>> {
        return dao.getCartDataList()
    }

    override fun addCartProduct(cartProduct: CartProduct): Single<CartProduct> {
        return dao.addCartProduct(cartProduct)
    }

    override fun deleteProductFromCart(productVariantId: String): Completable {
        return dao.deleteProductFromCart(productVariantId)
    }

    override fun deleteProductListFromCart(productVariantIdList: List<String>): Completable {
        return dao.deleteProductListFromCart(productVariantIdList)
    }

    override fun deleteAllProductsFromCart(): Completable {
        return dao.deleteAllProductsFromCart()
    }

    override fun changeCartProductQuantity(productVariantId: String, newQuantity: Int): Single<CartProduct> {
        return dao.changeCartProductQuantity(productVariantId, newQuantity)
    }

}