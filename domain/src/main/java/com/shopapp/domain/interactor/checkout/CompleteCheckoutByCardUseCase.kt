package com.shopapp.domain.interactor.checkout

import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.Order
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class CompleteCheckoutByCardUseCase @Inject constructor(
    private val checkoutRepository: CheckoutRepository
) : SingleUseCase<Order, CompleteCheckoutByCardUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Order> {
        return with(params) {
            checkoutRepository.completeCheckoutByCard(checkout, email, address, creditCardVaultToken)
        }
    }

    class Params(
        val checkout: Checkout,
        val email: String,
        val address: Address,
        val creditCardVaultToken: String
    )
}