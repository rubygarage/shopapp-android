package com.client.shop.ui.base.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.client.shop.R
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.ui.recycler.BaseRecyclerAdapter
import com.client.shop.ui.base.ui.recycler.EndlessRecyclerViewScrollListener
import com.client.shop.ui.base.ui.recycler.GridSpaceDecoration
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState

abstract class PaginationActivity<T, V : BaseMvpView, P : MvpPresenter<V>, VS : ViewState<V>> :
        BaseMvpActivity<V, P, VS>(), OnItemClickListener<T>, SwipeRefreshLayout.OnRefreshListener,
        BaseMvpView {

    protected var paginationValue: String? = null
    protected val dataList = mutableListOf<T>()
    protected lateinit var recycler: RecyclerView
    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var adapter: BaseRecyclerAdapter<T>

    companion object {
        private const val SPAN_COUNT = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setContentView(getContentView())
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }

    @LayoutRes
    protected open fun getContentView() = R.layout.activity_pagination

    protected open fun isGrid() = false

    abstract fun initAdapter(): BaseRecyclerAdapter<T>

    override fun onNewViewStateInstance() {

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected open fun setupRecyclerView() {

        adapter = initAdapter()
        recycler = findViewById(R.id.recyclerView)

        val layoutManager: RecyclerView.LayoutManager
        if (isGrid()) {
            layoutManager = GridLayoutManager(this, SPAN_COUNT)
            recycler.addItemDecoration(GridSpaceDecoration(resources.getDimensionPixelSize(R.dimen.recycler_padding), SPAN_COUNT))
        } else {
            layoutManager = LinearLayoutManager(this)
        }
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)
        recycler.addOnScrollListener(object : EndlessRecyclerViewScrollListener(recycler.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                fetchData()
            }
        })
    }

    protected open fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
    }

    override fun onRefresh() {
        paginationValue = null
        dataList.clear()
        adapter.notifyDataSetChanged()
        fetchData()
    }

    protected open fun fetchData() {

    }

    override fun showProgress() {
        swipeRefreshLayout.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout.isRefreshing = false
    }
}