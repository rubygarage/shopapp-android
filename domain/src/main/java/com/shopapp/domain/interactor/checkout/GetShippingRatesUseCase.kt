package com.shopapp.domain.interactor.checkout

import com.shopapp.gateway.entity.ShippingRate
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CheckoutRepository
import javax.inject.Inject

class GetShippingRatesUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<List<ShippingRate>, String>() {

    override fun buildUseCaseSingle(params: String) = checkoutRepository.getShippingRates(params)
}