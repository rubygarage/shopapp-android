package com.shopify.api.adapter

import com.client.shop.gateway.entity.Category
import com.shopify.buy3.Storefront
import com.shopify.constant.Constant.DEFAULT_STRING

object CategoryAdapter {

    fun adapt(shop: Storefront.Shop, collection: Storefront.Collection): Category {
        return Category(
            collection.id.toString(),
            collection.title,
            collection.description,
            collection.descriptionHtml ?: DEFAULT_STRING,
            ImageAdapter.adapt(collection.image),
            collection.updatedAt.toDate(),
            ProductListAdapter.adapt(shop, collection.products)
        )
    }
}
