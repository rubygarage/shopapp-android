package com.domain.interactor.recent

import com.domain.entity.Product
import com.domain.entity.SortType
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.ProductRepository
import io.reactivex.Single
import javax.inject.Inject

class RecentUseCase @Inject constructor(private val productRepository: ProductRepository) :
        SingleUseCase<List<Product>, RecentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<List<Product>> {
        return with(params) {
            //TODO MOVE REVERSE TO SHOPIFY
            productRepository.getProductList(perPage, paginationValue, SortType.RECENT, true)
        }
    }

    data class Params(
            val perPage: Int,
            val paginationValue: String?
    )
}