package com.shopapp.ui.category.contract

import com.shopapp.domain.interactor.category.GetCategoriesUseCase
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface CategoryListView : BaseLceView<List<Category>>

class CategoryListPresenter @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) :
    BaseLcePresenter<List<Category>, CategoryListView>(getCategoriesUseCase) {

    fun getCategoryList(perPage: Int, paginationValue: String?) {

        getCategoriesUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            GetCategoriesUseCase.Params(perPage, paginationValue)
        )
    }
}