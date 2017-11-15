package com.client.shop.ui.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.blog.adapter.BlogAdapter
import com.client.shop.ui.blog.contract.BlogPresenter
import com.client.shop.ui.blog.contract.BlogView
import com.client.shop.ui.blog.di.BlogModule
import com.domain.entity.Article
import javax.inject.Inject

class BlogActivity :
        PaginationActivity<Article, BlogView, BlogPresenter>(),
        BlogView {

    @Inject lateinit var blogPresenter: BlogPresenter

    companion object {
        fun getStartIntent(context: Context) = Intent(context, BlogActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = getString(R.string.new_in_blog)
        loadData(false)
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachBlogComponent(BlogModule()).inject(this)
    }

    override fun createPresenter() = blogPresenter

    //SETUP

    override fun setupAdapter() = BlogAdapter(dataList, this)

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadArticles(DEFAULT_PER_PAGE_COUNT, paginationValue)
    }

    override fun showContent(data: List<Article>) {
        super.showContent(data)
        if (data.isNotEmpty()) {
            paginationValue = data.last().paginationValue
            this.dataList.addAll(data)
            adapter.notifyDataSetChanged()
        }
    }

    //CALLBACK

    override fun onItemClicked(data: Article, position: Int) {
        startActivity(ArticleActivity.getStartIntent(this, data))
    }
}