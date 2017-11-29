package com.shopify.ui.checkout.contract

import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CartRepository
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import io.reactivex.Single
import javax.inject.Inject

interface CheckoutView : BaseView<Checkout>

class CheckoutPresenter @Inject constructor(private val checkoutUseCase: CheckoutUseCase) :
        BasePresenter<Checkout, CheckoutView>(arrayOf(checkoutUseCase)) {

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