package com.domain.interactor.category

import com.domain.entity.Category
import com.domain.entity.SortType
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CategoryRepository
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