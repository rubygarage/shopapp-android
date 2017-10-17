package com.shopify.api.adapter

import com.shopapicore.entity.CategoryDetails
import com.shopapicore.entity.Product
import com.shopify.buy3.Storefront

class CategoryDetailsAdapter(shop: Storefront.Shop, collection: Storefront.Collection) : CategoryDetails(
        collection.description,
        convertProductList(shop, collection.products)
) {

    companion object {

        private fun convertProductList(shop: Storefront.Shop, productConnection: Storefront.ProductConnection?): List<Product> {
            return if (productConnection != null) {
                ProductListAdapter(shop, productConnection.edges)
            } else {
                listOf()
            }
        }
    }
}