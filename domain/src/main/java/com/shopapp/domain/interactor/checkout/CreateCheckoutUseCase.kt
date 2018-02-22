package com.shopapp.domain.interactor.checkout

import com.shopapp.gateway.entity.CartProduct
import com.shopapp.gateway.entity.Checkout
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class CreateCheckoutUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, List<CartProduct>>() {

    override fun buildUseCaseSingle(params: List<CartProduct>): Single<Checkout> {
        return checkoutRepository.createCheckout(params)
    }
}