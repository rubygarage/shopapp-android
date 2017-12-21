package com.shopify.interactor.checkout

import com.domain.entity.Address
import com.domain.interactor.base.CompletableUseCase
import com.shopify.repository.CheckoutRepository
import io.reactivex.Completable
import javax.inject.Inject

class SetShippingAddressUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        CompletableUseCase<SetShippingAddressUseCase.Params>() {

    override fun buildUseCaseCompletable(params: Params): Completable {
        return checkoutRepository.setShippingAddress(params.checkoutId, params.address)
    }

    class Params(val checkoutId: String, val address: Address)
}