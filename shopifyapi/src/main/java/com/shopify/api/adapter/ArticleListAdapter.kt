package com.shopify.api.adapter

import com.shopapicore.entity.Article
import com.shopify.buy3.Storefront

import java.util.ArrayList

class ArticleListAdapter(edges: List<Storefront.ArticleEdge>) : ArrayList<Article>() {

    init {
        for (articleEdge in edges) {
            val adaptee = articleEdge.node
            val article = ArticleAdapter(adaptee)
            article.paginationValue = articleEdge.cursor
            add(article)
        }
    }
}
