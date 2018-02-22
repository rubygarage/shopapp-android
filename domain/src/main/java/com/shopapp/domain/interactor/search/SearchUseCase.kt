package com.shopapp.domain.interactor.search

import com.shopapp.gateway.entity.Product
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.ProductRepository
import io.reactivex.Single
import javax.inject.Inject

class SearchUseCase @Inject constructor(private val productRepository: ProductRepository) :
    SingleUseCase<List<Product>, SearchUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Product>> {
        return with(params) {
            productRepository.searchProductListByQuery(query, perPage, paginationValue)
        }
    }

    data class Params(
        val perPage: Int,
        val paginationValue: String?,
        val query: String
    )
}