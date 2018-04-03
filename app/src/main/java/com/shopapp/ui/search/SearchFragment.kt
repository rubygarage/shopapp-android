package com.shopapp.ui.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Product
import com.shopapp.ui.base.lce.view.LceEmptyView
import com.shopapp.ui.base.pagination.PaginationFragment
import com.shopapp.ui.base.recycler.divider.BackgroundItemDecoration
import com.shopapp.ui.product.adapter.ProductListAdapter
import com.shopapp.ui.search.contract.SearchPresenter
import com.shopapp.ui.search.contract.SearchView
import com.shopapp.ui.search.router.SearchRouter
import kotlinx.android.synthetic.main.fragment_search_with_categories_list.*
import javax.inject.Inject

class SearchFragment :
    PaginationFragment<Product, SearchView, SearchPresenter>(),
    SearchView {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    @Inject
    lateinit var router: SearchRouter

    private var lastQuery: String? = null
    private var currentQuery: String? = null

    //ANDROID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataList.clear()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachSearchComponent().inject(this)
    }

    override fun getContentView() = R.layout.fragment_search_with_categories_list

    override fun createPresenter() = searchPresenter

    //SETUP

    override fun isGrid() = true

    override fun setupAdapter(): ProductListAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_grid_item_size)
        return ProductListAdapter(MATCH_PARENT, size, dataList, this)
    }

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        recyclerView.addItemDecoration(BackgroundItemDecoration(R.color.white))
    }

    override fun setupEmptyView(emptyView: LceEmptyView) {
        emptyView.customiseEmptyImage(R.drawable.ic_search_not_found)
        emptyView.customiseEmptyMessage(R.string.no_results_found)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        currentQuery?.let { presenter.search(perPageCount(), paginationValue, it) }
    }

    override fun showContent(data: List<Product>) {
        super.showContent(data)

        if (lastQuery != null && lastQuery != currentQuery) {
            this.dataList.clear()
        }
        lastQuery = currentQuery
        this.dataList.addAll(data)
        adapter.notifyDataSetChanged()
        if (data.isNotEmpty()) {
            paginationValue = data.last().paginationValue
        }
        if (dataList.isEmpty() && currentQuery?.isNotBlank() == true) {
            showEmptyState()
        }
    }

    override fun hideProgress() {
        swipeRefreshLayout.isRefreshing = false
    }

    fun queryChanged(query: String) {
        if (query != currentQuery) {
            dataList.clear()
            paginationValue = null
            currentQuery = query
            adapter.notifyDataSetChanged()
            loadData(true)
        }
    }

    override fun onItemClicked(data: Product, position: Int) {
        router.showProduct(context, data.id)
    }
}