package com.shopapp.domain.interactor.product

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.ProductRepository
import com.shopapp.gateway.entity.Product
import com.shopapp.gateway.entity.SortType
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