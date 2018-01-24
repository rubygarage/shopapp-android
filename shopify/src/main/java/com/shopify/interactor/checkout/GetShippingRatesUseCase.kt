package com.shopify.interactor.checkout

import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.ShippingRate
import com.shopify.repository.CheckoutRepository
import javax.inject.Inject

class GetShippingRatesUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<List<ShippingRate>, String>() {

    override fun buildUseCaseSingle(params: String) = checkoutRepository.getShippingRates(params)
}