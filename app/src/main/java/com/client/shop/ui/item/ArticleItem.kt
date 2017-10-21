package com.client.shop.ui.item

import android.content.Context
import android.support.v7.widget.CardView
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.domain.entity.Article
import kotlinx.android.synthetic.main.item_article.view.*

class ArticleItem(context: Context) : CardView(context) {

    init {
        View.inflate(context, R.layout.item_article, this)
        layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        useCompatPadding = true
        preventCornerOverlap = true
    }

    fun setArticle(article: Article) {
        title.text = article.title
        content.text = article.content
        author.text = article.author.fullName
        val src = article.image?.src
        image.setImageURI(src)
        image.visibility = if (src == null) View.GONE else View.VISIBLE
    }
}