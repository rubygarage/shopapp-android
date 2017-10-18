package com.shopify.api.adapter

import com.shopapicore.entity.Product
import com.shopify.buy3.Storefront

class ProductListAdapter {

    companion object {

        fun adapt(shop: Storefront.Shop, productConnection: Storefront.ProductConnection?): List<Product> {
            return productConnection?.edges?.map { ProductAdapter.adapt(shop, it.node) } ?: listOf()
        }
    }
}
