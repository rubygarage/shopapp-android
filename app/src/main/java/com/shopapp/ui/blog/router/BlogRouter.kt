package com.shopapp.ui.blog.router

import android.content.Context
import com.shopapp.ui.blog.ArticleActivity
import com.shopapp.ui.blog.BlogActivity

class BlogRouter {

    fun showFullBlogList(context: Context?) {
        context?.let { it.startActivity(BlogActivity.getStartIntent(it)) }
    }

    fun showArticle(context: Context?, articleId: String) {
        context?.let { it.startActivity(ArticleActivity.getStartIntent(it, articleId)) }
    }
}