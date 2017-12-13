package com.client.shop.ui.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.ui.pagination.PaginationFragment
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.product.adapter.ProductAdapter
import com.client.shop.ui.search.contract.SearchPresenter
import com.client.shop.ui.search.contract.SearchView
import com.client.shop.ui.search.di.SearchModule
import com.domain.entity.Product
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment :
        PaginationFragment<Product, SearchView, SearchPresenter>(),
        SearchView, SearchToolbar.ToolbarSearchListener {

    @Inject lateinit var searchPresenter: SearchPresenter

    private var lastQuery: String? = null
    private var currentQuery: String? = null

    //ANDROID

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataList.clear()
        searchView.toolbarSearchListener = this
        val swipeProgressEndTarget = resources.getDimensionPixelSize(R.dimen.search_swipe_refresh_progress_end_target)
        swipeRefreshLayout?.setProgressViewEndTarget(false, swipeProgressEndTarget)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_cart, menu)
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachSearchComponent(SearchModule()).inject(this)
    }

    override fun isGrid() = true

    override fun getContentView() = R.layout.fragment_search

    override fun createPresenter() = searchPresenter

    //SETUP

    override fun setupAdapter(): ProductAdapter {
        val size = resources.getDimensionPixelSize(R.dimen.product_item_size)
        return ProductAdapter(MATCH_PARENT, size, dataList, this)
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

    //CALLBACK

    override fun onQueryChanged(query: String) {
        paginationValue = null
        currentQuery = query
        loadData(true)
    }

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(DetailsActivity.getStartIntent(context, data.id))
    }

    /**
     * @return 'true' if onBackPressed allowed
     */
    fun onBackPressed(): Boolean {
        return if (searchView.isToolbarExpanded()) {
            searchView.changeToolbarState()
            false
        } else {
            true
        }
    }
}