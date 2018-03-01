package com.shopapp.ui.product.contract

import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.domain.interactor.product.ProductListUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface ProductListView : BaseLceView<List<Product>>

class ProductListPresenter @Inject constructor(private val recentUseCase: ProductListUseCase) :
    BaseLcePresenter<List<Product>, ProductListView>(recentUseCase) {

    fun loadProductList(sortType: SortType, perPage: Int, paginationValue: String? = null,
                        keyword: String? = null, excludeKeyword: String? = null) {
        recentUseCase.execute(
            { view?.showContent(it) },
            { it.printStackTrace() },
            ProductListUseCase.Params(sortType, perPage, paginationValue, keyword, excludeKeyword)
        )
    }
}