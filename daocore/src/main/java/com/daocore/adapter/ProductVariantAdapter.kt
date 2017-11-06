package com.daocore.adapter

import com.daocore.entity.ProductVariantData
import com.daocore.entity.ProductVariantDataEntity
import com.domain.entity.ProductVariant

class ProductVariantAdapter {

    companion object {

        fun adaptToStore(adaptee: ProductVariant): ProductVariantData {
            val product = ProductVariantDataEntity()
            product.id = adaptee.id
            product.title = adaptee.title
            product.price = adaptee.price
            product.isAvailable = adaptee.isAvailable
            product.image = ImageAdapter.adaptToStore(adaptee.image)
            return product
        }

        fun adaptFromStore(adaptee: ProductVariantData): ProductVariant {
            return ProductVariant(
                    adaptee.id,
                    adaptee.title,
                    adaptee.price,
                    adaptee.isAvailable,
                    listOf(),
                    ImageAdapter.adaptFromStore(adaptee.image))
        }
    }
}