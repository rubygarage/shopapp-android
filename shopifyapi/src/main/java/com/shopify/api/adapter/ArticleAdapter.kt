package com.shopify.api.adapter

import com.shopapicore.entity.Article
import com.shopify.buy3.Storefront

class ArticleAdapter(adaptee: Storefront.Article) : Article(
        adaptee.id.toString(),
        adaptee.title,
        adaptee.content,
        ImageAdapter.newInstance(adaptee.image),
        AutorAdapter(adaptee.author),
        adaptee.tags,
        adaptee.blog.id.toString(),
        adaptee.blog.title,
        adaptee.publishedAt.toDate(),
        adaptee.url
)