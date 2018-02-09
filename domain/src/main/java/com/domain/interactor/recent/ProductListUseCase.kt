package com.domain.interactor.recent

import com.client.shop.getaway.entity.Product
import com.client.shop.getaway.entity.SortType
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.ProductRepository
import io.reactivex.Single
import javax.inject.Inject

class ProductListUseCase @Inject constructor(private val productRepository: ProductRepository) :
    SingleUseCase<List<Product>, ProductListUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Product>> {
        return with(params) {
            productRepository.getProductList(perPage, paginationValue, sortType, keyword, excludeKeyword)
        }
    }

    data class Params(
        val sortType: SortType,
        val perPage: Int,
        val paginationValue: String?,
        val keyword: String?,
        val excludeKeyword: String?
    )
}