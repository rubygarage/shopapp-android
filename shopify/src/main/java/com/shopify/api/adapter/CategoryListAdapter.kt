package com.shopify.api.adapter

import com.client.shop.getaway.entity.Category
import com.shopify.buy3.Storefront

object CategoryListAdapter {

    fun adapt(shop: Storefront.Shop): List<Category> {
        return shop.collections.edges.map {
            val category = CategoryAdapter.adapt(shop, it.node)
            category.paginationValue = it.cursor
            category
        }
    }
}
