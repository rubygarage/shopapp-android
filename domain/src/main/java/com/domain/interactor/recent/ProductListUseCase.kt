package com.domain.interactor.recent

import com.domain.entity.Product
import com.domain.entity.SortType
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.ProductRepository
import io.reactivex.Single
import javax.inject.Inject

class ProductListUseCase @Inject constructor(private val productRepository: ProductRepository) :
        SingleUseCase<List<Product>, ProductListUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Product>> {
        return with(params) {
            productRepository.getProductList(perPage, paginationValue, sortType, keyPhrase)
        }
    }

    data class Params(
            val sortType: SortType,
            val perPage: Int,
            val paginationValue: String?,
            val keyPhrase: String?
    )
}