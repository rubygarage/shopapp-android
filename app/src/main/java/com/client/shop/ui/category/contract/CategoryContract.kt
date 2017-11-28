package com.client.shop.ui.category.contract

import com.domain.entity.Category
import com.domain.entity.Product
import com.domain.entity.SortType
import com.domain.interactor.base.SingleUseCase
import com.repository.CategoryRepository
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import io.reactivex.Single
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

class CategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) :
        SingleUseCase<Category, CategoryUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Category> {
        return with(params) {
            //TODO MOVE TO SHOPIFY
            val reverse = sortType == SortType.RECENT
            categoryRepository.getCategory(categoryId, perPage, paginationValue, sortType, reverse)
        }
    }

    data class Params(
            val perPage: Int,
            val paginationValue: String?,
            val categoryId: String,
            val sortType: SortType
    )
}