package com.shopify.api.adapter

import com.shopapicore.entity.Author
import com.shopify.buy3.Storefront

class AutorAdapter(adaptee: Storefront.ArticleAuthor) : Author(
        adaptee.firstName,
        adaptee.lastName,
        adaptee.name,
        adaptee.email,
        adaptee.bio)