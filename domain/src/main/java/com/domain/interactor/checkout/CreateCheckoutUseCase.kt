package com.domain.interactor.checkout

import com.client.shop.getaway.entity.CartProduct
import com.client.shop.getaway.entity.Checkout
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class CreateCheckoutUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, List<CartProduct>>() {

    override fun buildUseCaseSingle(params: List<CartProduct>): Single<Checkout> {
        return checkoutRepository.createCheckout(params)
    }
}