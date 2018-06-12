package com.shopapp.domain.interactor.checkout

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.entity.Checkout
import io.reactivex.Single
import javax.inject.Inject

class GetCheckoutUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, String>() {

    override fun buildUseCaseSingle(params: String): Single<Checkout> {
        return checkoutRepository.getCheckout(params)
    }
}