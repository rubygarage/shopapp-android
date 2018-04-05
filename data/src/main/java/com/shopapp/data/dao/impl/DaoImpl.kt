package com.shopapp.data.dao.impl

import android.content.Context
import com.shopapp.data.dao.Dao
import com.shopapp.data.dao.adapter.CartProductAdapter
import com.shopapp.data.dao.entity.CartProductData
import com.shopapp.data.dao.entity.CartProductDataEntity
import com.shopapp.data.dao.entity.Models
import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.Error
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.TableCreationMode

class DaoImpl : Dao {

    companion object {
        const val MAX_QUANTITY = 999
        const val PRODUCT_NOT_FOUND_ERROR = "Product not found"
    }

    private val store: KotlinReactiveEntityStore<Persistable>

    constructor(context: Context) {
        val source = DatabaseSource(context, Models.DEFAULT, 1)
        source.setTableCreationMode(TableCreationMode.DROP_CREATE)
        store = KotlinReactiveEntityStore(KotlinEntityDataStore(source.configuration))
    }

    internal constructor(store: KotlinReactiveEntityStore<Persistable>) {
        this.store = store
    }

    override fun getCartDataList(): Observable<List<CartProduct>> {
        return store.select(CartProductDataEntity::class)
            .get()
            .observableResult()
            .map {
                val list: MutableList<CartProduct> = mutableListOf()
                it.iterator().forEach { list.add(CartProductAdapter.adaptFromStore(it)) }
                list
            }
    }

    override fun addCartProduct(cartProduct: CartProduct): Single<CartProduct> {
        val productVariant = cartProduct.productVariant
        var storeItem: CartProductData? = store.select(CartProductDataEntity::class)
            .where(CartProductDataEntity.ID.eq(productVariant.id))
            .get()
            .singleOrNull()
        if (storeItem != null) {
            val quantity = storeItem.quantity + cartProduct.quantity
            storeItem.quantity = if (quantity > MAX_QUANTITY) MAX_QUANTITY else quantity
        } else {
            storeItem = CartProductAdapter.adaptToStore(cartProduct)
        }
        return store.upsert(storeItem).map { CartProductAdapter.adaptFromStore(it) }
    }

    override fun deleteProductFromCart(productVariantId: String): Completable {
        val storeItem: CartProductData? = store.select(CartProductDataEntity::class)
            .where(CartProductDataEntity.ID.eq(productVariantId))
            .get()
            .singleOrNull()

        return storeItem?.let { store.delete(storeItem) } ?: Completable.complete()
    }

    override fun deleteProductListFromCart(productVariantListId: List<String>): Completable {
        return store.delete(CartProductDataEntity::class)
            .where(CartProductDataEntity.ID.`in`(productVariantListId))
            .get()
            .single()
            .toCompletable()
    }

    override fun deleteAllProductsFromCart(): Completable {
        return store
            .delete(CartProductDataEntity::class)
            .get()
            .single()
            .toCompletable()
    }

    override fun changeCartProductQuantity(
        productVariantId: String,
        newQuantity: Int
    ): Single<CartProduct> {
        val storeItem: CartProductData? = store.select(CartProductDataEntity::class)
            .where(CartProductDataEntity.ID.eq(productVariantId))
            .get()
            .singleOrNull()

        return storeItem?.let {
            it.quantity = newQuantity
            store.update(storeItem).map { CartProductAdapter.adaptFromStore(it) }
        } ?: Single.error(Error.NonCritical(PRODUCT_NOT_FOUND_ERROR))
    }
}