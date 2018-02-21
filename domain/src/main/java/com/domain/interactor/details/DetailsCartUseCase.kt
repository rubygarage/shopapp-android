package com.domain.interactor.details

import com.client.shop.gateway.entity.CartProduct
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CartRepository
import io.reactivex.Single
import javax.inject.Inject

class DetailsCartUseCase @Inject constructor(private val cartRepository: CartRepository) :
    SingleUseCase<CartProduct, CartProduct>() {

    override fun buildUseCaseSingle(params: CartProduct): Single<CartProduct> {
        return cartRepository.addCartProduct(params)
    }
}