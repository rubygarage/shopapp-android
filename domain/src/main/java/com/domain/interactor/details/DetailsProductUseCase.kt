package com.domain.interactor.details

import com.client.shop.getaway.entity.Product
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.ProductRepository
import io.reactivex.Single
import javax.inject.Inject

class DetailsProductUseCase @Inject constructor(private val productRepository: ProductRepository) :
    SingleUseCase<Product, String>() {

    override fun buildUseCaseSingle(params: String): Single<Product> {
        return productRepository.getProduct(params)
    }
}