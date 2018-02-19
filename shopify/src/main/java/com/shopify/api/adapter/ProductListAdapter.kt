package com.shopify.api.adapter

import com.client.shop.gateway.entity.Product
import com.shopify.buy3.Storefront

object ProductListAdapter {

    fun adapt(shop: Storefront.Shop, productConnection: Storefront.ProductConnection?): List<Product> =
        productConnection?.edges?.map { ProductAdapter.adapt(shop, it.node, it.cursor, false) } ?: listOf()
}
