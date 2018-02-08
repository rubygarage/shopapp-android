package com.shopify.interactor.checkout

import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
import com.shopify.repository.CheckoutRepository
import javax.inject.Inject

class SetShippingRateUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, SetShippingRateUseCase.Params>() {

    override fun buildUseCaseSingle(params: SetShippingRateUseCase.Params) = with(params) {
        checkoutRepository.selectShippingRate(checkoutId, shippingRate)
    }

    class Params(
        val checkoutId: String,
        val shippingRate: ShippingRate
    )
}