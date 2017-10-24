package com.client.shop.ui.search.contract

import android.text.TextUtils
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Product
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface SearchView : BaseMvpView {

    fun searchResultsReceived(productList: List<Product>, query: String)
}

class SearchPresenter @Inject constructor(repository: Repository) : BasePresenter<SearchView>(repository) {

    fun search(perPage: Int, paginationValue: String?, query: String) {

        if (TextUtils.isEmpty(query)) {
            view.searchResultsReceived(listOf(), query)
            return
        }

        showProgress()

        val searchDisposable = repository.searchProductListByQuery(query, perPage, paginationValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.searchResultsReceived(result, query)
                        view.hideProgress()
                    }
                }, { error ->
                    error.printStackTrace()
                    hideProgress()
                })
        disposables.add(searchDisposable)
    }
}