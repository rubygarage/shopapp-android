package com.shopify.api.adapter

import com.shopapicore.entity.Category
import com.shopify.buy3.Storefront

import java.util.ArrayList

class CategoryListAdapter(shop: Storefront.Shop) : ArrayList<Category>() {

    init {
        val collectionEdges = shop.collections.edges
        for (collectionEdge in collectionEdges) {
            val collection = collectionEdge.node
            val category = CategoryAdapter(shop, collection)
            category.paginationValue = collectionEdge.cursor
            add(category)
        }
    }
}
