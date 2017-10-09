package com.client.shop.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.client.shop.R
import com.client.shop.di.component.AppComponent
import com.client.shop.ui.base.rx.RxQueryTextListener
import com.client.shop.ui.base.ui.PaginationActivity
import com.client.shop.ui.base.ui.ProductAdapter
import com.client.shop.ui.details.DetailsActivity
import com.client.shop.ui.search.contract.SearchPresenter
import com.shopapicore.entity.Product
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import com.client.shop.ui.search.contract.SearchView as MvpSearchView


class SearchActivity : PaginationActivity<Product, MvpSearchView, SearchPresenter, SearchViewState>(),
        MvpSearchView {

    @Inject lateinit var searchPresenter: SearchPresenter
    private var searchObserver: Observable<String>? = null
    private var searchSubscription: Disposable? = null
    private var lastQuery: String? = null

    companion object {
        private const val PER_PAGE = 10
        private const val SEARCH_DEBOUNCE = 500L

        fun getStartIntent(context: Context) = Intent(context, SearchActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.search)
        setupSearch()
    }

    override fun onResume() {
        super.onResume()
        searchObserver?.let {
            searchSubscription = it
                    .debounce(SEARCH_DEBOUNCE, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                    .subscribe({
                        paginationValue = null
                        fetchData()
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

    override fun inject(component: AppComponent) {
        component.inject(this)
    }

    override fun onItemClicked(data: Product, position: Int) {
        startActivity(DetailsActivity.getStartIntent(this, data))
    }

    override fun isGrid() = true

    override fun initAdapter() = ProductAdapter(dataList, this)

    override fun getContentView() = R.layout.activity_search

    override fun createPresenter(): SearchPresenter {
        return searchPresenter
    }

    override fun createViewState() = SearchViewState()

    private fun setupSearch() {
        searchView.setIconifiedByDefault(false)
        searchObserver = Observable.create<String> { emitter ->
            searchView.setOnQueryTextListener(RxQueryTextListener(emitter))
        }
    }

    override fun fetchData() {
        val query = searchView.query
        if (query != null) {
            presenter.search(PER_PAGE, paginationValue, query.toString())
        }
    }

    override fun searchResultsReceived(productList: List<Product>, query: String) {
        if (lastQuery != null && lastQuery != query) {
            this.dataList.clear()
        }
        lastQuery = query
        this.dataList.addAll(productList)
        adapter.notifyDataSetChanged()
        if (productList.isNotEmpty()) {
            paginationValue = productList.last().paginationValue
        }
        viewState.setSearchData(this.dataList, query)
    }
}