package com.client.shop.ui.item

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ext.setResizedImageUri
import com.client.shop.gateway.entity.Article
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleItem(context: Context) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.item_article, this)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    fun setArticle(article: Article) {
        titleTextView.text = article.title
        content.text = article.content
        author.text = article.author.fullName
        val src = article.image?.src
        image.visibility = if (src == null) {
            GONE
        } else {
            image.setResizedImageUri(context, src)
            VISIBLE
        }
    }
}