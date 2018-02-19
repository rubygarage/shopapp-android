package com.domain.interactor.checkout

import com.client.shop.gateway.entity.Checkout
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class GetCheckoutUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, String>() {

    override fun buildUseCaseSingle(params: String): Single<Checkout> {
        return checkoutRepository.getCheckout(params)
    }
}