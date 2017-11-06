package com.shopify.api.adapter

import com.domain.entity.Article
import com.shopify.buy3.Storefront

class ArticleListAdapter {

    companion object {

        fun adapt(edges: List<Storefront.ArticleEdge>): List<Article> {
            return edges.map { ArticleAdapter.adapt(it.node, it.cursor) }
        }
    }
}
