package com.shopapp.domain.interactor.checkout

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CheckoutRepository
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.ShippingRate
import javax.inject.Inject

class SetShippingRateUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, SetShippingRateUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params) = with(params) {
        checkoutRepository.setShippingRate(checkoutId, shippingRate)
    }

    class Params(
        val checkoutId: String,
        val shippingRate: ShippingRate
    )
}