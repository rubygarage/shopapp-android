package com.data.dao.adapter

import com.data.dao.entity.CartProductData
import com.data.dao.entity.CartProductDataEntity
import com.client.shop.getaway.entity.CartProduct

object CartProductAdapter {

    fun adaptFromStore(adaptee: CartProductData): CartProduct {
        return CartProduct(
            ProductVariantAdapter.adaptFromStore(adaptee.productVariant),
            adaptee.title,
            adaptee.currency,
            adaptee.quantity
        )
    }

    fun adaptToStore(adaptee: CartProduct): CartProductData {
        val cartItem = CartProductDataEntity()
        cartItem.id = adaptee.productVariant.id
        cartItem.productVariant = ProductVariantAdapter.adaptToStore(adaptee.productVariant)
        cartItem.title = adaptee.title
        cartItem.currency = adaptee.currency
        cartItem.quantity = adaptee.quantity
        return cartItem
    }
}