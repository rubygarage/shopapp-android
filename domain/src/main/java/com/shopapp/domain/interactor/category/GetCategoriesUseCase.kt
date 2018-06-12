package com.shopapp.domain.interactor.category

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CategoryRepository
import com.shopapp.gateway.entity.Category
import io.reactivex.Single
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(private val repository: CategoryRepository) :
    SingleUseCase<List<Category>, GetCategoriesUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Category>> {
        return with(params) {
            repository.getCategories(perPage, paginationValue)
        }
    }

    data class Params(
        val perPage: Int,
        val paginationValue: String?
    )
}