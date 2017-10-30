package com.client.shop.ui.search.contract

import android.text.TextUtils
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Product
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface SearchView : BaseMvpView<List<Product>> {

    fun setQuery(query: String)
}

class SearchPresenter @Inject constructor(repository: Repository) : BasePresenter<List<Product>, SearchView>(repository) {

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