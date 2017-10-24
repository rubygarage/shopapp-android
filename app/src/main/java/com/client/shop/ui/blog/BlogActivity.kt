package com.client.shop.ui.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.PaginationActivity
import com.client.shop.ui.blog.adapter.BlogAdapter
import com.client.shop.ui.blog.contract.BlogPresenter
import com.client.shop.ui.blog.contract.BlogView
import com.client.shop.ui.blog.di.BlogModule
import com.domain.entity.Article
import javax.inject.Inject

class BlogActivity : PaginationActivity<Article, BlogView, BlogPresenter, BlogViewState>(), BlogView {

    @Inject lateinit var blogPresenter: BlogPresenter

    companion object {
        private const val ARTICLES_COUNT = 10

        fun getStartIntent(context: Context) = Intent(context, BlogActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.new_in_blog)
    }

    override fun inject(component: AppComponent) {
        component.attachBlogComponent(BlogModule()).inject(this)
    }

    override fun createPresenter() = blogPresenter

    override fun createViewState() = BlogViewState()

    override fun onNewViewStateInstance() {
        fetchData()
    }

    override fun fetchData() {
        presenter.loadArticles(ARTICLES_COUNT, paginationValue)
    }

    override fun initAdapter() = BlogAdapter(dataList, this)

    override fun articleListLoaded(articleList: List<Article>) {
        if (articleList.isNotEmpty()) {
            paginationValue = articleList.last().paginationValue
            this.dataList.addAll(articleList)
            adapter.notifyDataSetChanged()
        }
        viewState.setData(this.dataList)
    }

    override fun onItemClicked(data: Article, position: Int) {
        startActivity(ArticleActivity.getStartIntent(this, data))
    }
}