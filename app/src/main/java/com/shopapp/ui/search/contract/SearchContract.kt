package com.shopapp.ui.search.contract

import com.shopapp.domain.interactor.search.SearchUseCase
import com.shopapp.gateway.entity.Product
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface SearchView : BaseLceView<List<Product>> {

    fun hideProgress()
}

class SearchPresenter @Inject constructor(private val searchUseCase: SearchUseCase) :
    BaseLcePresenter<List<Product>, SearchView>(searchUseCase) {

    fun search(perPage: Int, paginationValue: String?, query: String) {
        if (query.isEmpty()) {
            view?.showContent(listOf())
            return
        }
        searchUseCase.execute(
            { view?.showContent(it) },
            {
                it.printStackTrace()
                view?.hideProgress()
            },
            SearchUseCase.Params(perPage, paginationValue, query)
        )
    }
}