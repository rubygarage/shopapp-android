package com.client.shop.ui.product.contract

import com.domain.entity.Product
import com.domain.entity.SortType
import com.domain.interactor.recent.ProductListUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface ProductListView : BaseLceView<List<Product>>

class ProductListPresenter @Inject constructor(private val recentUseCase: ProductListUseCase) :
        BaseLcePresenter<List<Product>, ProductListView>(recentUseCase) {

    fun loadProductList(sortType: SortType, perPage: Int, paginationValue: String? = null) {

        recentUseCase.execute(
                { view?.showContent(it) },
                { it.printStackTrace() },
                ProductListUseCase.Params(sortType, perPage, paginationValue)
        )
    }
}