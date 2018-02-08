package com.shopify.api.adapter

import com.domain.entity.Article
import com.shopify.buy3.Storefront

object ArticleListAdapter {

    fun adapt(edges: List<Storefront.ArticleEdge>): List<Article> =
        edges.map { ArticleAdapter.adapt(it.node, it.cursor) }
}
