package com.client.shop.ui.product.contract

import com.client.shop.getaway.entity.Product
import com.client.shop.getaway.entity.SortType
import com.domain.interactor.recent.ProductListUseCase
import com.client.shop.ui.base.contract.BaseLcePresenter
import com.client.shop.ui.base.contract.BaseLceView
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