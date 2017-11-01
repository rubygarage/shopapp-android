package com.daocore

import android.content.Context
import com.daocore.adapter.CartItemAdapter
import com.daocore.adapter.ProductVariantAdapter
import com.daocore.entity.CartItemData
import com.daocore.entity.CartItemDataEntity
import com.daocore.entity.Models
import com.domain.entity.CartItem
import com.domain.entity.ProductVariant
import io.reactivex.Flowable
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

    override fun getCartItems(): Flowable<CartItem> {
        return store.select(CartItemDataEntity::class)
                .get()
                .flowable()
                .map { CartItemAdapter.adaptFromStore(it) }
    }

    override fun addProductToCart(productVariant: ProductVariant, quantity: Int) {
        var storeItem: CartItemData? = store.select(CartItemDataEntity::class)
                .where(CartItemDataEntity.ID.eq(productVariant.id))
                .get()
                .singleOrNull()
        if (storeItem != null) {
            storeItem.quantity = storeItem.quantity + quantity
        } else {
            val dataEntity = CartItemDataEntity()
            dataEntity.id = productVariant.id
            dataEntity.productVariant = ProductVariantAdapter.adaptToStore(productVariant)
            dataEntity.quantity = quantity
            storeItem = dataEntity
        }
        store.upsert(storeItem).blockingGet()
    }

    override fun deleteProductFromCart(productVariantId: String) {
        val storeItem: CartItemData? = store.select(CartItemDataEntity::class)
                .where(CartItemDataEntity.ID.eq(productVariantId))
                .get()
                .singleOrNull()
        storeItem?.let { store.delete(storeItem).blockingGet() }
    }

    override fun changeCartProductQuantity(productVariantId: String, newQuantity: Int) {
        val storeItem: CartItemData? = store.select(CartItemDataEntity::class)
                .where(CartItemDataEntity.ID.eq(productVariantId))
                .get()
                .singleOrNull()
        storeItem?.let {
            it.quantity = newQuantity
            store.update(storeItem).blockingGet()
        }
    }
}