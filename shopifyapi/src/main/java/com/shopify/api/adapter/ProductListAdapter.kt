package com.shopify.api.adapter

import com.shopapicore.entity.Product
import com.shopify.buy3.Storefront

import java.util.ArrayList

class ProductListAdapter(shop: Storefront.Shop, edges: List<Storefront.ProductEdge>) : ArrayList<Product>() {

    init {
        for (productEdge in edges) {
            val adaptee = productEdge.node
            val product = ProductAdapter(shop, adaptee)
            product.paginationValue = productEdge.cursor
            add(product)
        }
    }
}
