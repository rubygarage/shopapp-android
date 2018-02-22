package com.shopapp.domain.interactor.category

import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.SortType
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CategoryRepository
import io.reactivex.Single
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) :
    SingleUseCase<Category, CategoryUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Category> {
        return with(params) {
            categoryRepository.getCategory(categoryId, perPage, paginationValue, sortType)
        }
    }

    data class Params(
        val perPage: Int,
        val paginationValue: String?,
        val categoryId: String,
        val sortType: SortType
    )
}