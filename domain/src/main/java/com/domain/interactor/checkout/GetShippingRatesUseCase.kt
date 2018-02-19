package com.domain.interactor.checkout

import com.client.shop.gateway.entity.ShippingRate
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CheckoutRepository
import javax.inject.Inject

class GetShippingRatesUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<List<ShippingRate>, String>() {

    override fun buildUseCaseSingle(params: String) = checkoutRepository.getShippingRates(params)
}