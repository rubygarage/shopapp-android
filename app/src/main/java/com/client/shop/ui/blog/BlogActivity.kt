package com.client.shop.ui.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.blog.adapter.BlogAdapter
import com.client.shop.ui.blog.contract.BlogPresenter
import com.client.shop.ui.blog.contract.BlogView
import com.client.shop.ui.blog.di.BlogModule
import com.domain.entity.Article
import com.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import javax.inject.Inject

class BlogActivity :
    PaginationActivity<Article, BlogView, BlogPresenter>(),
    BlogView {

    @Inject
    lateinit var blogPresenter: BlogPresenter

    companion object {
        fun getStartIntent(context: Context) = Intent(context, BlogActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.blog_posts))
        loadData()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachBlogComponent(BlogModule()).inject(this)
    }

    override fun createPresenter() = blogPresenter

    //SETUP

    override fun setupAdapter() = BlogAdapter(dataList, this)

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_line))
        recycler.addItemDecoration(divider)
        recycler.setBackgroundResource(R.color.white)
    }

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
        startActivity(ArticleActivity.getStartIntent(this, data.id))
    }
}