package com.data.dao.adapter

import com.data.dao.entity.ProductVariantData
import com.data.dao.entity.ProductVariantDataEntity
import com.domain.entity.ProductVariant

object ProductVariantAdapter {

    fun adaptToStore(adaptee: ProductVariant): ProductVariantData {
        val product = ProductVariantDataEntity()
        product.id = adaptee.id
        product.title = adaptee.title
        product.price = adaptee.price
        product.isAvailable = adaptee.isAvailable
        product.image = ImageAdapter.adaptToStore(adaptee.image)
        product.productId = adaptee.productId
        product.productImage = ImageAdapter.adaptToStore(adaptee.productImage)
        return product
    }

    fun adaptFromStore(adaptee: ProductVariantData): ProductVariant {
        return ProductVariant(
                adaptee.id,
                adaptee.title,
                adaptee.price,
                adaptee.isAvailable,
                listOf(),
                ImageAdapter.adaptFromStore(adaptee.image),
                ImageAdapter.adaptFromStore(adaptee.productImage),
                adaptee.productId
        )
    }
}