package com.shopapp.ui.category.contract

import com.shopapp.domain.interactor.category.CategoryListUseCase
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface CategoryListView : BaseLceView<List<Category>>

class CategoryListPresenter @Inject constructor(
    private val categoryListUseCase: CategoryListUseCase
) :
    BaseLcePresenter<List<Category>, CategoryListView>(categoryListUseCase) {

    fun getCategoryList(perPage: Int, paginationValue: String?) {

        categoryListUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            CategoryListUseCase.Params(perPage, paginationValue)
        )
    }
}