package com.shopify.api.adapter

import com.domain.entity.ProductVariant
import com.shopify.buy3.Storefront

object ProductVariantAdapter {

    fun adapt(adaptee: Storefront.ProductVariant): ProductVariant {
        return ProductVariant(adaptee.id.toString(),
                adaptee.title,
                adaptee.price.toFloat(),
                adaptee.availableForSale == true,
                VariantOptionListAdapter.adapt(adaptee.selectedOptions),
                ImageAdapter.adapt(adaptee.image))
    }
}