package com.shopify.api.adapter

import com.shopapicore.entity.Author
import com.shopify.buy3.Storefront

class AutorAdapter {

    companion object {

        fun adapt(adaptee: Storefront.ArticleAuthor): Author {
            return Author(
                    adaptee.firstName,
                    adaptee.lastName,
                    adaptee.name,
                    adaptee.email,
                    adaptee.bio)
        }
    }
}