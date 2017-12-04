package com.shopify.ui.checkout.contract

import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CartRepository
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import io.reactivex.Single
import javax.inject.Inject

interface CheckoutView : BaseLceView<Checkout>

class CheckoutPresenter @Inject constructor(private val checkoutUseCase: CheckoutUseCase) :
        BaseLcePresenter<Checkout, CheckoutView>(checkoutUseCase) {

    fun createCheckout() {

        checkoutUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                Unit
        )
    }
}

class CheckoutUseCase @Inject constructor(
        private val cartRepository: CartRepository,
        private val checkoutRepository: CheckoutRepository
) :
        SingleUseCase<Checkout, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Checkout> {
        return cartRepository.getCartProductList()
                .first(listOf())
                .flatMap { checkoutRepository.createCheckout(it) }
    }
}