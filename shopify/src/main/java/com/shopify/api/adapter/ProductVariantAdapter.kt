package com.shopify.api.adapter

import com.domain.entity.Image
import com.domain.entity.ProductVariant
import com.shopify.buy3.Storefront

object ProductVariantAdapter {

    fun adapt(adaptee: Storefront.ProductVariant): ProductVariant {
        return ProductVariant(
            adaptee.id.toString(),
            adaptee.title,
            adaptee.price.toFloat(),
            adaptee.availableForSale == true,
            VariantOptionListAdapter.adapt(adaptee.selectedOptions),
            ImageAdapter.adapt(adaptee.image),
            convertImage(adaptee.product).first(),
            adaptee.product.id.toString()
        )
    }

    private fun convertImage(productAdaptee: Storefront.Product): List<Image> =
        productAdaptee.images.edges.map { ImageAdapter.adapt(it.node)!! }
}