package com.shopify.interactor.checkout

import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCheckoutUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, String>() {

    override fun buildUseCaseSingle(params: String): Single<Checkout> {
        return checkoutRepository.getCheckout(params)
    }
}