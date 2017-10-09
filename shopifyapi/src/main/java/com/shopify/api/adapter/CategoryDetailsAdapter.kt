package com.shopify.api.adapter

import com.shopapicore.entity.CategoryDetails
import com.shopify.buy3.Storefront

class CategoryDetailsAdapter(shop: Storefront.Shop, collection: Storefront.Collection) : CategoryDetails(
        collection.description,
        if (collection.products != null) {
            ProductListAdapter(shop, collection.products.edges)
        } else {
            listOf()
        }
)