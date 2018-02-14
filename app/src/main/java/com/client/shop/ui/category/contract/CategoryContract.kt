package com.client.shop.ui.category.contract

import com.client.shop.getaway.entity.Product
import com.client.shop.getaway.entity.SortType
import com.domain.interactor.category.CategoryUseCase
import com.client.shop.ui.base.contract.BaseLcePresenter
import com.client.shop.ui.base.contract.BaseLceView
import javax.inject.Inject

interface CategoryView : BaseLceView<List<Product>>

class CategoryPresenter @Inject constructor(private val categoryUseCase: CategoryUseCase) :
    BaseLcePresenter<List<Product>, CategoryView>(categoryUseCase) {

    fun loadProductList(perPage: Int, paginationValue: String?, categoryId: String, sortType: SortType) {

        categoryUseCase.execute(
            { view?.showContent(it.productList) },
            { it.printStackTrace() },
            CategoryUseCase.Params(perPage, paginationValue, categoryId, sortType)
        )
    }
}