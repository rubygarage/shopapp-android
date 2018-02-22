package com.shopapp.domain.interactor.details

import com.shopapp.gateway.entity.Product
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.ProductRepository
import io.reactivex.Single
import javax.inject.Inject

class DetailsProductUseCase @Inject constructor(private val productRepository: ProductRepository) :
    SingleUseCase<Product, String>() {

    override fun buildUseCaseSingle(params: String): Single<Product> {
        return productRepository.getProduct(params)
    }
}