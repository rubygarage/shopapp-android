package com.shopify.interactor.checkout

import com.domain.entity.Address
import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class SetShippingAddressUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        SingleUseCase<Checkout, SetShippingAddressUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Checkout> {
        return checkoutRepository.setShippingAddress(params.checkoutId, params.address)
    }

    class Params(val checkoutId: String, val address: Address)
}