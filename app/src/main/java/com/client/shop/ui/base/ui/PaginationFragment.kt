package com.client.shop.ui.base.ui

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.client.shop.R
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.ui.recycler.BaseRecyclerAdapter
import com.client.shop.ui.base.ui.recycler.EndlessRecyclerViewScrollListener
import com.client.shop.ui.base.ui.recycler.GridSpaceDecoration
import com.client.shop.ui.base.ui.recycler.OnItemClickListener
import com.hannesdorfmann.mosby3.mvp.MvpPresenter
import com.hannesdorfmann.mosby3.mvp.viewstate.ViewState

abstract class PaginationFragment<T, V : BaseMvpView, P : MvpPresenter<V>, VS : ViewState<V>> :
        BaseMvpFragment<V, P, VS>(), OnItemClickListener<T>, SwipeRefreshLayout.OnRefreshListener,
        BaseMvpView {

    protected val dataList = mutableListOf<T>()
    protected var paginationValue: String? = null
    protected var recycler: RecyclerView? = null
    protected var swipeRefreshLayout: SwipeRefreshLayout? = null
    protected var adapter: BaseRecyclerAdapter<T>? = null

    companion object {
        private const val SPAN_COUNT = 2
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(getContentView(), container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                activity.onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    protected open fun setupRecyclerView() {

        adapter = initAdapter()
        recycler = view?.findViewById(R.id.recyclerView)

        recycler?.let {
            val layoutManager: RecyclerView.LayoutManager
            if (isGrid()) {
                layoutManager = GridLayoutManager(context, SPAN_COUNT)
                it.addItemDecoration(GridSpaceDecoration(resources.getDimensionPixelSize(R.dimen.recycler_padding), SPAN_COUNT))
            } else {
                layoutManager = LinearLayoutManager(context)
            }
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.setHasFixedSize(true)
            it.addOnScrollListener(object : EndlessRecyclerViewScrollListener(it.layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    fetchData()
                }
            })
        }
    }

    protected open fun setupSwipeRefreshLayout() {
        swipeRefreshLayout = view?.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.let {
            it.setOnRefreshListener(this)
            it.setColorSchemeResources(R.color.colorAccent)
        }
    }

    override fun onRefresh() {
        paginationValue = null
        dataList.clear()
        adapter?.notifyDataSetChanged()
        fetchData()
    }

    protected open fun fetchData() {

    }

    override fun showProgress() {
        swipeRefreshLayout?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefreshLayout?.isRefreshing = false
    }
}