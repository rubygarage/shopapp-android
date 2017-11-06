package com.daocore.adapter

import com.daocore.entity.CartItemData
import com.daocore.entity.CartItemDataEntity
import com.domain.entity.CartProduct

class CartItemAdapter {

    companion object {

        fun adaptFromStore(adaptee: CartItemData): CartProduct {
            return CartProduct(
                    ProductVariantAdapter.adaptFromStore(adaptee.productVariant),
                    adaptee.productId,
                    adaptee.currency,
                    adaptee.quantity
            )
        }

        fun adaptToStore(adaptee: CartProduct): CartItemData {
            val cartItem = CartItemDataEntity()
            cartItem.id = adaptee.productVariant.id
            cartItem.productVariant = ProductVariantAdapter.adaptToStore(adaptee.productVariant)
            cartItem.productId = adaptee.productId
            cartItem.currency = adaptee.currency
            cartItem.quantity = adaptee.quantity
            return cartItem
        }
    }
}