package com.shopapp.ui.base.recycler

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager


abstract class EndlessRecyclerViewScrollListener(
    private val layoutManager: RecyclerView.LayoutManager,
    private var withHeader: Boolean = false) : RecyclerView.OnScrollListener() {

    private var visibleThreshold = 5
    private var currentPage = 0
    private var previousTotalItemCount = 0
    private var loading = true
    private val startingPageIndex = 0

    init {
        if (layoutManager is GridLayoutManager) {
            visibleThreshold *= layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            visibleThreshold *= layoutManager.spanCount
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }

    override fun onScrolled(view: RecyclerView?, dx: Int, dy: Int) {
        var lastVisibleItemPosition = 0

        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                lastVisibleItemPosition = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
            is LinearLayoutManager -> lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()
        }

        if (itemsWithoutHeaderTotalCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = itemsWithoutHeaderTotalCount
            if (itemsWithoutHeaderTotalCount == 0) {
                this.loading = true
            }
        }

        if (loading && itemsWithoutHeaderTotalCount > previousTotalItemCount) {
            loading = false
            previousTotalItemCount = itemsWithoutHeaderTotalCount
        }


        if (!loading && lastVisibleItemPosition + visibleThreshold > itemsTotalCount) {
            currentPage++
            onLoadMore(currentPage, itemsWithoutHeaderTotalCount)
            loading = true
        }
    }

    private val itemsTotalCount: Int
        get() = layoutManager.itemCount

    private val itemsWithoutHeaderTotalCount: Int
        get() = if (withHeader) itemsTotalCount - 1 else itemsTotalCount


    abstract fun onLoadMore(page: Int, totalItemsCount: Int)
}