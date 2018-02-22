package com.shopapp.data.dao.adapter

import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.data.dao.entity.ProductVariantData
import com.shopapp.data.dao.entity.ProductVariantDataEntity

object ProductVariantAdapter {

    fun adaptToStore(adaptee: ProductVariant): ProductVariantData {
        val product = ProductVariantDataEntity()
        product.id = adaptee.id
        product.title = adaptee.title
        product.price = adaptee.price.toFloat()
        product.isAvailable = adaptee.isAvailable
        product.selectedOptions = adaptee.selectedOptions.map { VariantOptionAdapter.adaptToStore(it) }
        product.image = ImageAdapter.adaptToStore(adaptee.image)
        product.productItemId = adaptee.productId
        product.productImage = ImageAdapter.adaptToStore(adaptee.productImage)
        return product
    }

    fun adaptFromStore(adaptee: ProductVariantData): ProductVariant {
        return ProductVariant(
            adaptee.id,
            adaptee.title,
            adaptee.price.toBigDecimal(),
            adaptee.isAvailable,
            adaptee.selectedOptions.map { VariantOptionAdapter.adaptFromStore(it) },
            ImageAdapter.adaptFromStore(adaptee.image),
            ImageAdapter.adaptFromStore(adaptee.productImage),
            adaptee.productItemId
        )
    }
}