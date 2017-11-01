package com.daocore.adapter

import com.daocore.entity.CartItemData
import com.daocore.entity.CartItemDataEntity
import com.domain.entity.CartItem

class CartItemAdapter {

    companion object {

        fun adaptToStore(adaptee: CartItem): CartItemData {
            val cartItem = CartItemDataEntity()
            cartItem.id = adaptee.productVariant.id
            cartItem.productVariant = ProductVariantAdapter.adaptToStore(adaptee.productVariant)
            cartItem.quantity = adaptee.quantity
            return cartItem
        }

        fun adaptFromStore(adaptee: CartItemData): CartItem {
            return CartItem(ProductVariantAdapter.adaptFromStore(adaptee.productVariant), adaptee.quantity)
        }
    }
}