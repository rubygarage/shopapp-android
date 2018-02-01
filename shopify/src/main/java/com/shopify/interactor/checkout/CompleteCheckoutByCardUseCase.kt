package com.shopify.interactor.checkout

import com.domain.entity.Address
import com.domain.entity.Order
import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
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