package com.shopapp.ui.blog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Article
import com.shopapp.ui.base.pagination.PaginationActivity
import com.shopapp.ui.blog.adapter.BlogAdapter
import com.shopapp.ui.blog.contract.BlogPresenter
import com.shopapp.ui.blog.contract.BlogView
import com.shopapp.ui.blog.router.BlogRouter
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import javax.inject.Inject

class BlogActivity :
    PaginationActivity<Article, BlogView, BlogPresenter>(),
    BlogView {

    @Inject
    lateinit var blogPresenter: BlogPresenter

    @Inject
    lateinit var router: BlogRouter

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
        ShopApplication.appComponent.attachBlogComponent().inject(this)
    }

    override fun createPresenter() = blogPresenter

    //SETUP

    override fun setupAdapter() = BlogAdapter(dataList, this)

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        ContextCompat.getDrawable(this, R.drawable.divider_line)?.let {
            divider.setDrawable(it)
        }
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
        router.showArticle(this, data.id)
    }
}