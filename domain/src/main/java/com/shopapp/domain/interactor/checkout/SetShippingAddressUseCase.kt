package com.shopapp.domain.interactor.checkout

import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Checkout
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class SetShippingAddressUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<Checkout, SetShippingAddressUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Checkout> {
        return checkoutRepository.setShippingAddress(params.checkoutId, params.address)
    }

    class Params(val checkoutId: String, val address: Address)
}