package com.client.shop.ui.search.contract

import android.text.TextUtils
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.contract.BaseView
import com.client.shop.ui.base.contract.SingleUseCase
import com.domain.entity.Product
import com.repository.ProductRepository
import io.reactivex.Single
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

class SearchUseCase @Inject constructor(private val productRepository: ProductRepository) :
        SingleUseCase<List<Product>, SearchUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Product>> {
        return with(params) {
            productRepository.searchProductListByQuery(query, perPage, paginationValue)
        }
    }

    data class Params(
            val perPage: Int,
            val paginationValue: String?,
            val query: String
    )
}