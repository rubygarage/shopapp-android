package com.shopify.interactor.checkout

import com.domain.entity.CartProduct
import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class CreateCheckoutUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        SingleUseCase<Checkout, List<CartProduct>>() {

    override fun buildUseCaseSingle(params: List<CartProduct>): Single<Checkout> {
        return checkoutRepository.createCheckout(params)
    }
}