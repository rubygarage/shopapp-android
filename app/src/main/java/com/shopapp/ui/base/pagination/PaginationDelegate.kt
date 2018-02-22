package com.shopapp.ui.base.pagination

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.shopapp.R
import com.shopapp.ui.base.recycler.EndlessRecyclerViewScrollListener
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.base.recycler.divider.GridSpaceDecoration

class PaginationDelegate<M> {

    companion object {
        private const val SPAN_COUNT = 2
    }

    fun setupRecyclerView(paginationListener: PaginationListener, recycler: RecyclerView?,
                          adapter: BaseRecyclerAdapter<M>, isGrid: Boolean, perPageCount: Int) {
        recycler?.let {
            val context = recycler.context
            val layoutManager: RecyclerView.LayoutManager
            if (isGrid) {
                layoutManager = GridLayoutManager(context, SPAN_COUNT)
                val space = context.resources.getDimensionPixelSize(R.dimen.recycler_divider_space)
                val decoration = GridSpaceDecoration(space, SPAN_COUNT)
                it.addItemDecoration(decoration)
            } else {
                layoutManager = LinearLayoutManager(context)
            }
            it.layoutManager = layoutManager
            it.adapter = adapter
            it.setHasFixedSize(true)
            it.addOnScrollListener(object : EndlessRecyclerViewScrollListener(it.layoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int) {
                    if (totalItemsCount >= perPageCount)
                        paginationListener.loadNewPage()
                }
            })
        }
    }

    fun setupSwipeRefreshLayout(
        swipeRefreshLayout: SwipeRefreshLayout?,
        refreshListener: SwipeRefreshLayout.OnRefreshListener
    ) {
        swipeRefreshLayout?.let {
            it.setOnRefreshListener(refreshListener)
            it.setColorSchemeResources(R.color.colorPrimary)
        }
    }

    interface PaginationListener {

        fun loadNewPage()
    }
}