package com.shopapp.ui.base.pagination

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import com.shopapp.R
import com.shopapp.gateway.entity.Error
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT

abstract class PaginationActivity<M, V : BaseLceView<List<M>>, P : BaseLcePresenter<List<M>, V>> :
    BaseLceActivity<List<M>, V, P>(),
    OnItemClickListener,
    SwipeRefreshLayout.OnRefreshListener,
    PaginationDelegate.PaginationListener {

    private val paginationDelegate: PaginationDelegate<M> = PaginationDelegate()
    protected val dataList = mutableListOf<M>()
    protected var paginationValue: String? = null
    protected lateinit var recycler: RecyclerView
    protected lateinit var swipeRefreshLayout: SwipeRefreshLayout
    protected lateinit var adapter: BaseRecyclerAdapter<M>

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
        paginationDelegate.setupRecyclerView(
            this,
            recycler,
            adapter,
            isGrid(),
            perPageCount()
        )
    }

    protected open fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        paginationDelegate.setupSwipeRefreshLayout(swipeRefreshLayout, this)
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

    override fun showError(error: Error) {
        super.showError(error)
        swipeRefreshLayout.isRefreshing = false
    }

    //CALLBACK

    override fun loadNewPage() {
        loadData(true)
    }

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