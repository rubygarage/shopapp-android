package com.shopapp.magento.adapter

import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.ProductVariant

object ProductVariantAdapter {

    fun adapt(product: Product): ProductVariant {
        val image = product.images.firstOrNull()
        return ProductVariant(
            product.id,
            product.title,
            product.price,
            true,
            listOf(),
            image,
            image,
            product.id
        )
    }
}