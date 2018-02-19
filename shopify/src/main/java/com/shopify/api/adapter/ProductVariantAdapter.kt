package com.shopify.api.adapter

import com.client.shop.gateway.entity.Image
import com.client.shop.gateway.entity.ProductVariant
import com.shopify.buy3.Storefront

object ProductVariantAdapter {

    fun adapt(adaptee: Storefront.ProductVariant): ProductVariant {
        return ProductVariant(
            adaptee.id.toString(),
            adaptee.title,
            adaptee.price,
            adaptee.availableForSale == true,
            VariantOptionListAdapter.adapt(adaptee.selectedOptions),
            ImageAdapter.adapt(adaptee.image),
            convertImage(adaptee.product)?.firstOrNull(),
            adaptee.product.id.toString()
        )
    }

    private fun convertImage(productAdaptee: Storefront.Product?): List<Image>? =
        productAdaptee?.images?.edges?.map { ImageAdapter.adapt(it.node)!! }
}