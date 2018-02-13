package com.client.shop.ui.base.ui.pagination

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.client.shop.R
import com.client.shop.ui.base.ui.recycler.EndlessRecyclerViewScrollListener
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import com.ui.base.lce.BaseActivity
import com.ui.base.recycler.OnItemClickListener
import com.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.ui.base.recycler.divider.GridSpaceDecoration
import com.ui.const.Constant.DEFAULT_PER_PAGE_COUNT

abstract class PaginationActivity<M, V : BaseLceView<List<M>>, P : BaseLcePresenter<List<M>, V>> :
    BaseActivity<List<M>, V, P>(),
    OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener {

    protected var paginationValue: String? = null
    protected val dataList = mutableListOf<M>()
    protected lateinit var recycler: RecyclerView
    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var adapter: BaseRecyclerAdapter<M>

    companion object {
        private const val SPAN_COUNT = 2
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }

    //INIT

    override fun getContentView() = R.layout.activity_pagination

    protected open fun isGrid() = false

    protected open fun perPageCount() = DEFAULT_PER_PAGE_COUNT

    //SETUP

    abstract fun setupAdapter(): BaseRecyclerAdapter<M>

    @CallSuper
    protected open fun setupRecyclerView() {

        adapter = setupAdapter()
        recycler = findViewById(R.id.recyclerView)

        val layoutManager: RecyclerView.LayoutManager
        if (isGrid()) {
            layoutManager = GridLayoutManager(this, SPAN_COUNT)
            recycler.addItemDecoration(GridSpaceDecoration(resources.getDimensionPixelSize(R.dimen.recycler_divider_space), SPAN_COUNT))
        } else {
            layoutManager = LinearLayoutManager(this)
        }
        recycler.layoutManager = layoutManager
        recycler.adapter = adapter
        recycler.addOnScrollListener(object : EndlessRecyclerViewScrollListener(recycler.layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                if (totalItemsCount >= perPageCount())
                    loadData(true)
            }
        })
    }

    protected open fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        swipeRefreshLayout.isRefreshing = true
    }

    override fun showContent(data: List<M>) {
        super.showContent(data)
        swipeRefreshLayout.isRefreshing = false
    }

    override fun showError(isNetworkError: Boolean) {
        super.showError(isNetworkError)
        swipeRefreshLayout.isRefreshing = false
    }

    //CALLBACK

    override fun onItemClicked(position: Int) {
        if (position >= 0 && dataList.size >= position) {
            onItemClicked(dataList[position], position)
        }
    }

    abstract fun onItemClicked(data: M, position: Int)

    override fun onRefresh() {
        paginationValue = null
        dataList.clear()
        adapter.notifyDataSetChanged()
        loadData(true)
    }
}