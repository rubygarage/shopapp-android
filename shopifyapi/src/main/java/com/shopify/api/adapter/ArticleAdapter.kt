package com.shopify.api.adapter

import com.shopapicore.entity.Article
import com.shopify.buy3.Storefront

class ArticleAdapter {

    companion object {

        fun adapt(adaptee: Storefront.Article): Article {
            return Article(
                    adaptee.id.toString(),
                    adaptee.title,
                    adaptee.content,
                    ImageAdapter.adapt(adaptee.image),
                    AutorAdapter.adapt(adaptee.author),
                    adaptee.tags,
                    adaptee.blog.id.toString(),
                    adaptee.blog.title,
                    adaptee.publishedAt.toDate(),
                    adaptee.url)
        }
    }
}