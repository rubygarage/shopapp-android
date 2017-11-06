package com.daocore

import android.content.Context
import com.daocore.adapter.CartItemAdapter
import com.daocore.entity.CartItemData
import com.daocore.entity.CartItemDataEntity
import com.daocore.entity.Models
import com.domain.entity.CartProduct
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.requery.Persistable
import io.requery.android.sqlite.DatabaseSource
import io.requery.reactivex.KotlinReactiveEntityStore
import io.requery.sql.KotlinEntityDataStore
import io.requery.sql.TableCreationMode

class DaoImpl(context: Context) : Dao {

    private val store: KotlinReactiveEntityStore<Persistable>

    init {
        val source = DatabaseSource(context, Models.DEFAULT, 1)
        source.setTableCreationMode(TableCreationMode.DROP_CREATE)
        store = KotlinReactiveEntityStore(KotlinEntityDataStore(source.configuration))
    }

    override fun getCartDataList(): Observable<List<CartProduct>> {
        return store.select(CartItemDataEntity::class)
                .get()
                .observableResult()
                .map { it -> it.asIterable() }
                .map {
                    val list: MutableList<CartProduct> = mutableListOf()
                    it.iterator().forEach { list.add(CartItemAdapter.adaptFromStore(it)) }
                    list
                }
    }

    override fun addCartProduct(cartProduct: CartProduct): Single<CartProduct> {
        val productVariant = cartProduct.productVariant
        var storeItem: CartItemData? = store.select(CartItemDataEntity::class)
                .where(CartItemDataEntity.ID.eq(productVariant.id))
                .get()
                .singleOrNull()
        if (storeItem != null) {
            storeItem.quantity = storeItem.quantity + cartProduct.quantity
        } else {
            storeItem = CartItemAdapter.adaptToStore(cartProduct)
        }
        return store.upsert(storeItem).map { CartItemAdapter.adaptFromStore(it) }
    }

    override fun deleteProductFromCart(productVariantId: String): Completable? {
        val storeItem: CartItemData? = store.select(CartItemDataEntity::class)
                .where(CartItemDataEntity.ID.eq(productVariantId))
                .get()
                .singleOrNull()
        return storeItem?.let { store.delete(storeItem) }
    }

    override fun changeCartProductQuantity(productVariantId: String, newQuantity: Int): Single<CartProduct>? {
        val storeItem: CartItemData? = store.select(CartItemDataEntity::class)
                .where(CartItemDataEntity.ID.eq(productVariantId))
                .get()
                .singleOrNull()
        return storeItem?.let {
            it.quantity = newQuantity
            store.update(storeItem).map { CartItemAdapter.adaptFromStore(it) }
        }
    }
}