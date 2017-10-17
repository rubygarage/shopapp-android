package com.shopify.api.adapter

import com.shopapicore.entity.Category
import com.shopify.buy3.Storefront

class CategoryAdapter(shop: Storefront.Shop, collection: Storefront.Collection) : Category(
        collection.id.toString(),
        collection.title,
        collection.description,
        ImageAdapter.newInstance(collection.image),
        collection.updatedAt.toDate(),
        CategoryDetailsAdapter(shop, collection)
)
