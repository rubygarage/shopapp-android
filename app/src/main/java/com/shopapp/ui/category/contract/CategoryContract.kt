package com.shopapp.ui.category.contract

import com.shopapp.domain.interactor.category.CategoryUseCase
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface CategoryView : BaseLceView<List<Product>>

class CategoryPresenter @Inject constructor(private val categoryUseCase: CategoryUseCase) :
    BaseLcePresenter<List<Product>, CategoryView>(categoryUseCase) {

    fun loadProductList(perPage: Int, paginationValue: String?, categoryId: String, sortType: SortType) {

        categoryUseCase.execute(
            {
                if (paginationValue == null && it.productList.isEmpty()) {
                    view?.showEmptyState()
                } else {
                    view?.showContent(it.productList)
                }
            },
            { resolveError(it) },
            CategoryUseCase.Params(perPage, paginationValue, categoryId, sortType)
        )
    }
}