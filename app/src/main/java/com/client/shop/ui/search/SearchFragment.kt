package com.client.shop.ui.search

import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.ui.pagination.PaginationFragment
import com.client.shop.ui.product.ProductDetailsActivity
import com.client.shop.ui.product.adapter.ProductListAdapter
import com.client.shop.ui.search.contract.SearchPresenter
import com.client.shop.ui.search.contract.SearchView
import com.client.shop.ui.search.di.SearchModule
import com.domain.entity.Product
import com.ui.base.recycler.divider.BackgroundItemDecoration
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment :
    PaginationFragment<Product, SearchView, SearchPresenter>(),
    SearchView {

    @Inject
    lateinit var searchPresenter: SearchPresenter

    private var lastQuery: String? = null
    private var currentQuery: String? = null

    //ANDROID

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataList.clear()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachSearchComponent(SearchModule()).inject(this)
    }

    override fun getContentView() = R.layout.fragment_search

    override fun createPresenter() = searchPresenter

    //SETUP

    override fun isGrid() = true

    override fun setupAdapter(): ProductListAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        return ProductListAdapter(MATCH_PARENT, size, dataList, this)
    }

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        recyclerView.addItemDecoration(BackgroundItemDecoration(R.color.white))
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
        adapter?.notifyDataSetChanged()
        if (data.isNotEmpty()) {
            paginationValue = data.last().paginationValue
        }
    }

    override fun hideProgress() {
        swipeRefreshLayout?.isRefreshing = false
    }

    fun queryChanged(query: String) {
        if (query != currentQuery) {
            paginationValue = null
            currentQuery = query
            loadData(true)
        }
    }

    override fun onItemClicked(data: Product, position: Int) {
        context?.let { startActivity(ProductDetailsActivity.getStartIntent(it, data.id)) }
    }
}