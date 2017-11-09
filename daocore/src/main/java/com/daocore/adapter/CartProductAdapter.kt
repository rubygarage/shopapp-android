package com.daocore.adapter

import com.daocore.entity.CartProductData
import com.daocore.entity.CartProductDataEntity
import com.domain.entity.CartProduct

class CartProductAdapter {

    companion object {

        fun adaptFromStore(adaptee: CartProductData): CartProduct {
            return CartProduct(
                    ProductVariantAdapter.adaptFromStore(adaptee.productVariant),
                    adaptee.productId,
                    adaptee.currency,
                    adaptee.quantity
            )
        }

        fun adaptToStore(adaptee: CartProduct): CartProductData {
            val cartItem = CartProductDataEntity()
            cartItem.id = adaptee.productVariant.id
            cartItem.productVariant = ProductVariantAdapter.adaptToStore(adaptee.productVariant)
            cartItem.productId = adaptee.productId
            cartItem.currency = adaptee.currency
            cartItem.quantity = adaptee.quantity
            return cartItem
        }
    }
}