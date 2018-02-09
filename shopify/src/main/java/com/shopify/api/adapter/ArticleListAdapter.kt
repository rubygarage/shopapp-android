package com.shopify.api.adapter

import com.client.shop.getaway.entity.Article
import com.shopify.buy3.Storefront

object ArticleListAdapter {

    fun adapt(edges: List<Storefront.ArticleEdge>): List<Article> =
        edges.map { ArticleAdapter.adapt(it.node, it.cursor) }
}
