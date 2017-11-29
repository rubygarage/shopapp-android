package com.client.shop.ui.search.contract

import android.text.TextUtils
import com.domain.entity.Product
import com.domain.interactor.search.SearchUseCase
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import javax.inject.Inject

interface SearchView : BaseView<List<Product>> {

    fun setQuery(query: String)
}

class SearchPresenter @Inject constructor(private val searchUseCase: SearchUseCase) :
        BasePresenter<List<Product>, SearchView>(arrayOf(searchUseCase)) {

    fun search(perPage: Int, paginationValue: String?, query: String) {

        if (TextUtils.isEmpty(query)) {
            view?.setQuery(query)
            view?.showContent(listOf())
            return
        }
        searchUseCase.execute(
                {
                    view?.setQuery(query)
                    view?.showContent(it)
                },
                { it.printStackTrace() },
                SearchUseCase.Params(perPage, paginationValue, query)
        )
    }
}