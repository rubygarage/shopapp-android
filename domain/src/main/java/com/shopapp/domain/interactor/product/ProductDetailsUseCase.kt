package com.shopapp.domain.interactor.product

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.ProductRepository
import com.shopapp.gateway.entity.Product
import io.reactivex.Single
import javax.inject.Inject

class ProductDetailsUseCase @Inject constructor(private val productRepository: ProductRepository) :
    SingleUseCase<Product, String>() {

    override fun buildUseCaseSingle(params: String): Single<Product> {
        return productRepository.getProduct(params)
    }
}