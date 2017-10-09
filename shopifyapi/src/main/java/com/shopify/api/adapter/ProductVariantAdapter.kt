package com.shopify.api.adapter

import com.shopapicore.entity.ProductVariant
import com.shopify.buy3.Storefront

class ProductVariantAdapter(productVariant: Storefront.ProductVariant) : ProductVariant(
        productVariant.id.toString(),
        productVariant.title,
        productVariant.price.toString(),
        productVariant.availableForSale == java.lang.Boolean.TRUE,
        if (productVariant.image != null) ImageAdapter(productVariant.image) else null
)