package com.client.shop.ui.category.contract

import com.domain.entity.Product
import com.domain.entity.SortType
import com.domain.interactor.category.CategoryUseCase
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import javax.inject.Inject

interface CategoryView : BaseView<List<Product>>

class CategoryPresenter @Inject constructor(private val categoryUseCase: CategoryUseCase) :
        BasePresenter<List<Product>, CategoryView>(arrayOf(categoryUseCase)) {

    fun loadProductList(perPage: Int, paginationValue: String?, categoryId: String, sortType: SortType) {

        categoryUseCase.execute(
                { view?.showContent(it.productList) },
                { it.printStackTrace() },
                CategoryUseCase.Params(perPage, paginationValue, categoryId, sortType)
        )
    }
}