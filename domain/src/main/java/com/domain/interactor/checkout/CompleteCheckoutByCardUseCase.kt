package com.domain.interactor.checkout

import com.client.shop.getaway.entity.Address
import com.client.shop.getaway.entity.Checkout
import com.client.shop.getaway.entity.Order
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CheckoutRepository
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