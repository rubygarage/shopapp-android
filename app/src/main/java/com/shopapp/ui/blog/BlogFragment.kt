package com.shopapp.ui.blog

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Article
import com.shopapp.ui.base.lce.BaseLceFragment
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.divider.DividerItemDecorator
import com.shopapp.ui.base.ui.FragmentVisibilityListener
import com.shopapp.ui.blog.adapter.BlogAdapter
import com.shopapp.ui.blog.contract.BlogPresenter
import com.shopapp.ui.blog.contract.BlogView
import com.shopapp.ui.blog.router.BlogRouter
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT
import kotlinx.android.synthetic.main.fragment_blog.*
import javax.inject.Inject

class BlogFragment :
    BaseLceFragment<List<Article>, BlogView, BlogPresenter>(),
    BlogView,
    OnItemClickListener {

    @Inject
    lateinit var blogPresenter: BlogPresenter

    @Inject
    lateinit var router: BlogRouter

    private var articleList = mutableListOf<Article>()
    private lateinit var adapter: BlogAdapter
    var visibilityListener: FragmentVisibilityListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        seeAll.setOnClickListener {
            router.showFullBlogList(context)
        }
        changeSeeAllState()
        setupRecycler()
        loadData(true)
    }

    override fun inject() {
        ShopApplication.appComponent.attachBlogComponent().inject(this)
    }

    //SETUP

    private fun setupRecycler() {
        adapter = BlogAdapter(articleList, this)
        val layoutManager = LinearLayoutManager(context)
        val divider =
            DividerItemDecorator(context, R.drawable.divider_line)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.addItemDecoration(divider)
    }

    private fun changeSeeAllState() {
        if (articleList.size == DEFAULT_PER_PAGE_COUNT) {
            seeAll.visibility = View.VISIBLE
        } else {
            seeAll.visibility = View.GONE
        }
    }

    //INITIAL

    override fun createPresenter() = blogPresenter

    override fun getContentView() = R.layout.fragment_blog

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadArticles(DEFAULT_PER_PAGE_COUNT)
    }

    override fun showContent(data: List<Article>) {
        super.showContent(data)
        visibilityListener?.changeVisibility(data.isNotEmpty())
        if (data.isNotEmpty()) {
            this.articleList.clear()
            this.articleList.addAll(data)
            adapter.notifyDataSetChanged()
            changeSeeAllState()
        }
    }

    //CALLBACK

    override fun onItemClicked(position: Int) {
        articleList.getOrNull(position)?.let {
            router.showArticle(context, it.id)
        }
    }

}