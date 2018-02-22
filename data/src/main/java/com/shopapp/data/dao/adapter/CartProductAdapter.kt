package com.shopapp.data.dao.adapter

import com.shopapp.gateway.entity.CartProduct
import com.shopapp.data.dao.entity.CartProductData
import com.shopapp.data.dao.entity.CartProductDataEntity

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