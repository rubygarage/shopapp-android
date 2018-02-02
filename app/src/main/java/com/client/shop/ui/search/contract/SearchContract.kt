package com.client.shop.ui.search.contract

import android.text.TextUtils
import com.domain.entity.Product
import com.domain.interactor.search.SearchUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface SearchView : BaseLceView<List<Product>> {

    fun hideProgress()
}

class SearchPresenter @Inject constructor(private val searchUseCase: SearchUseCase) :
    BaseLcePresenter<List<Product>, SearchView>(searchUseCase) {

    fun search(perPage: Int, paginationValue: String?, query: String) {

        if (TextUtils.isEmpty(query)) {
            view?.showContent(listOf())
            return
        }
        searchUseCase.executeWithDisposeLatest(
            { view?.showContent(it) },
            {
                it.printStackTrace()
                view?.hideProgress()
            },
            SearchUseCase.Params(perPage, paginationValue, query)
        )
    }
}