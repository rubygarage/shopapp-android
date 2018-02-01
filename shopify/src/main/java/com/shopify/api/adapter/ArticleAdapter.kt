package com.shopify.api.adapter

import com.domain.entity.Article
import com.shopify.buy3.Storefront

object ArticleAdapter {

    fun adapt(adaptee: Storefront.Article, paginationValue: String? = null): Article {
        return Article(
            adaptee.id.toString(),
            adaptee.title,
            adaptee.content,
            adaptee.contentHtml,
            ImageAdapter.adapt(adaptee.image),
            AuthorAdapter.adapt(adaptee.author),
            adaptee.tags,
            adaptee.blog.id.toString(),
            adaptee.blog.title,
            adaptee.publishedAt.toDate(),
            adaptee.url,
            paginationValue)
    }
}