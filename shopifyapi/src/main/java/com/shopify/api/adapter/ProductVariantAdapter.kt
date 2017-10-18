package com.shopify.api.adapter

import com.shopapicore.entity.ProductVariant
import com.shopify.buy3.Storefront

class ProductVariantAdapter {

    companion object {

        fun adapt(adaptee: Storefront.ProductVariant): ProductVariant {
            return ProductVariant(adaptee.id.toString(),
                    adaptee.title,
                    adaptee.price.toString(),
                    adaptee.availableForSale == true,
                    VariantOptionListAdapter.adapt(adaptee.selectedOptions),
                    ImageAdapter.adapt(adaptee.image))
        }
    }
}