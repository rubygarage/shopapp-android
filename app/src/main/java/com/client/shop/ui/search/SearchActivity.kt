package com.client.shop.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.rx.RxQueryTextListener
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.base.ui.recycler.adapter.ProductAdapter
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.search.contract.SearchPresenter
import com.client.shop.ui.search.di.SearchModule
import com.domain.entity.Product
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.client.shop.ui.search.contract.SearchView as MvpSearchView


class SearchActivity :
        PaginationActivity<Product, MvpSearchView, SearchPresenter>(),
        MvpSearchView {

    @Inject lateinit var searchPresenter: SearchPresenter
    private var searchObserver: Observable<String>? = null
    private var searchSubscription: Disposable? = null
    private var lastQuery: String? = null
    private var currentQuery: String? = null

    companion object {
        private const val SEARCH_DEBOUNCE = 500L

        fun getStartIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.search)
        setupSearch()
        val swipeProgressEndTarget = resources.getDimensionPixelSize(R.dimen.search_swipe_refresh_progress_end_target)
        swipeRefreshLayout.setProgressViewEndTarget(false, swipeProgressEndTarget)
    }

    override fun onResume() {
        super.onResume()
        searchObserver?.let {
            searchSubscription = it
                    .debounce(SEARCH_DEBOUNCE, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        paginationValue = null
                        loadData(true)
                    }, { error -> error.printStackTrace() })
        }
    }

    override fun onPause() {
        super.onPause()
        searchSubscription?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)
        return true
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachSearchComponent(SearchModule()).inject(this)
    }

    override fun isGrid() = true

    override fun getContentView() = R.layout.activity_search

    override fun createPresenter(): SearchPresenter {
        return searchPresenter
    }

    //SETUP

    override fun setupAdapter() = ProductAdapter(dataList, this)

    private fun setupSearch() {
        searchView.setIconifiedByDefault(false)
        searchObserver = Observable.create<String> { emitter ->
            searchView.setOnQueryTextListener(RxQueryTextListener(emitter))
        }
    }

    //LCE

    override fun setQuery(query: String) {
        currentQuery = query
    }

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        val query = searchView.query
        if (query != null) {
            presenter.search(perPageCount(), paginationValue, query.toString())
        }
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
    }

    //CALLBACK

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(DetailsActivity.getStartIntent(this, data.id))
    }
}