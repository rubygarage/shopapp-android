package com.shopify.api.adapter

import com.domain.entity.Image
import com.domain.entity.ProductVariant
import com.shopify.buy3.Storefront

object ProductVariantAdapter {

    fun adapt(adaptee: Storefront.ProductVariant, productImage: Image?): ProductVariant {
        val variantImage = ImageAdapter.adapt(adaptee.image)
        return ProductVariant(
                adaptee.id.toString(),
                adaptee.title,
                adaptee.price.toFloat(),
                adaptee.availableForSale == true,
                VariantOptionListAdapter.adapt(adaptee.selectedOptions),
                variantImage ?: productImage
        )
    }
}