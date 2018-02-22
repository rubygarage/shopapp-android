package com.shopapp.domain.interactor.details

import com.shopapp.gateway.entity.CartProduct
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CartRepository
import io.reactivex.Single
import javax.inject.Inject

class DetailsCartUseCase @Inject constructor(private val cartRepository: CartRepository) :
    SingleUseCase<CartProduct, CartProduct>() {

    override fun buildUseCaseSingle(params: CartProduct): Single<CartProduct> {
        return cartRepository.addCartProduct(params)
    }
}