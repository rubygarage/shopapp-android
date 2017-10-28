package com.client.shop.ui.search.contract

import android.text.TextUtils
import com.client.shop.ui.base.contract.BaseMvpViewLce
import com.client.shop.ui.base.contract.BasePresenterLce
import com.domain.entity.Product
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface SearchView : BaseMvpViewLce<List<Product>> {

    fun setQuery(query: String)
}

class SearchPresenter @Inject constructor(repository: Repository) : BasePresenterLce<List<Product>, SearchView>(repository) {

    fun search(perPage: Int, paginationValue: String?, query: String) {

        if (TextUtils.isEmpty(query)) {
            view?.setQuery(query)
            view?.showContent(listOf())
            return
        }

        val searchDisposable = repository.searchProductListByQuery(query, perPage, paginationValue)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    view?.setQuery(query)
                    view?.showContent(result)
                },
                        { error -> error.printStackTrace() })
        disposables.add(searchDisposable)
    }
}