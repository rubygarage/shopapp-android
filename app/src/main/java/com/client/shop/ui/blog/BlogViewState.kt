package com.client.shop.ui.blog

import android.os.Bundle
import com.client.shop.ui.blog.contract.BlogView
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState
import com.domain.entity.Article

class BlogViewState : RestorableViewState<BlogView> {

    private var data: List<Article>? = null

    companion object {
        private const val KEY_ARTICLE_LIST = "BlogViewState-article-list"
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<BlogView>? {
        if (`in` == null) {
            return null
        }

        data = `in`.getParcelableArrayList(KEY_ARTICLE_LIST)

        return this
    }

    override fun saveInstanceState(out: Bundle) {
        if (data != null) {
            out.putParcelableArrayList(KEY_ARTICLE_LIST, ArrayList(data))
        }
    }

    override fun apply(view: BlogView?, retained: Boolean) {
        data?.let {
            view?.articleListLoaded(it)
            view?.hideProgress()
        }
    }

    fun setData(data: List<Article>) {
        this.data = data
    }
}