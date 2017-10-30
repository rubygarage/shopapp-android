package com.client.shop.ui.base.ui.pagination

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import com.client.shop.R
import com.client.shop.const.Constant.DEFAULT_PER_PAGE_COUNT
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.ui.lce.BaseActivity
import com.client.shop.ui.base.ui.recycler.EndlessRecyclerViewScrollListener
import com.client.shop.ui.base.ui.recycler.GridSpaceDecoration
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.client.shop.ui.base.ui.recycler.adapter.BaseRecyclerAdapter

abstract class PaginationActivity<M, V : BaseMvpView<List<M>>, P : BasePresenter<List<M>, V>> :
        BaseActivity<List<M>, V, P>(),
        OnItemClickListener<M>,
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
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupRecyclerView()
        setupSwipeRefreshLayout()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //INIT

    override fun getContentView() = R.layout.activity_pagination

    protected open fun isGrid() = false

    protected open fun perPageCount() = DEFAULT_PER_PAGE_COUNT

    //SETUP

    abstract fun setupAdapter(): BaseRecyclerAdapter<M>

    protected open fun setupRecyclerView() {

        adapter = setupAdapter()
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
                if (totalItemsCount >= perPageCount())
                    loadData(true)
            }
        })
    }

    protected open fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent)
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

    override fun onRefresh() {
        paginationValue = null
        dataList.clear()
        adapter.notifyDataSetChanged()
        loadData(true)
    }
}