package com.domain.interactor.checkout

import com.client.shop.gateway.entity.Checkout
import com.client.shop.gateway.entity.ShippingRate
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CheckoutRepository
import javax.inject.Inject

class SetShippingRateUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, SetShippingRateUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params) = with(params) {
        checkoutRepository.selectShippingRate(checkoutId, shippingRate)
    }

    class Params(
        val checkoutId: String,
        val shippingRate: ShippingRate
    )
}