package com.client.shop.ui.blog

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.ui.BaseMvpFragment
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.blog.adapter.BlogAdapter
import com.client.shop.ui.blog.contract.BlogPresenter
import com.client.shop.ui.blog.contract.BlogView
import com.shopapicore.entity.Article
import kotlinx.android.synthetic.main.fragment_blog.*
import javax.inject.Inject

class BlogFragment : BaseMvpFragment<BlogView, BlogPresenter, BlogViewState>(), BlogView, OnItemClickListener<Article> {

    @Inject lateinit var blogPresenter: BlogPresenter
    private var articleList = mutableListOf<Article>()
    private lateinit var adapter: BlogAdapter

    companion object {
        private const val ARTICLES_COUNT = 10
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_blog, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
    }

    private fun setupRecycler() {
        adapter = BlogAdapter(articleList, this, View.OnClickListener { startActivity(BlogActivity.getStartIntent(context)) })
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = false
    }

    override fun inject(component: AppComponent) {
        component.inject(this)
    }

    override fun createPresenter() = blogPresenter

    override fun createViewState() = BlogViewState()

    override fun onNewViewStateInstance() {
        presenter.loadArticles(ARTICLES_COUNT)
    }

    override fun onItemClicked(data: Article, position: Int) {
        startActivity(ArticleActivity.getStartIntent(context, data))
    }

    override fun articleListLoaded(articleList: List<Article>) {
        viewState.setData(articleList)
        this.articleList.clear()
        this.articleList.addAll(articleList)
        adapter.withFooter = this.articleList.size == ARTICLES_COUNT
        adapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }
}