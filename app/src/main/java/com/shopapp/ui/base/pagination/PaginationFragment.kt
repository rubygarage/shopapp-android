package com.shopapp.ui.base.pagination

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import com.shopapp.R
import com.shopapp.gateway.entity.Error
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import com.shopapp.ui.base.lce.BaseLceFragment
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.const.Constant.DEFAULT_PER_PAGE_COUNT

abstract class PaginationFragment<M, V : BaseLceView<List<M>>, P : BaseLcePresenter<List<M>, V>> :
    BaseLceFragment<List<M>, V, P>(),
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupSwipeRefreshLayout()
        setupRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (item.itemId == android.R.id.home) {
                activity?.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //INIT

    override fun getContentView() = R.layout.activity_pagination

    protected open fun isGrid() = false

    fun perPageCount() = DEFAULT_PER_PAGE_COUNT

    //SETUP

    abstract fun setupAdapter(): BaseRecyclerAdapter<M>

    protected open fun setupRecyclerView() {
        view?.let {
            adapter = setupAdapter()
            recycler = it.findViewById(R.id.recyclerView)
            paginationDelegate.setupRecyclerView(this,
                recycler,
                adapter,
                isGrid(),
                perPageCount())
        }
    }

    protected open fun setupSwipeRefreshLayout() {
        view?.let {
            swipeRefreshLayout = it.findViewById(R.id.swipeRefreshLayout)
            swipeRefreshLayout.setOnRefreshListener(this)
            swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
        }
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        if (pullToRefresh) {
            swipeRefreshLayout.isRefreshing = true
        }
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