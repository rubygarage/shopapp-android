package com.shopify.ui.checkout.contract

import com.domain.interactor.base.SingleUseCase
import com.repository.CartRepository
import com.repository.CheckoutRepository
import com.ui.contract.BasePresenter
import com.ui.contract.BaseView
import io.reactivex.Single
import javax.inject.Inject

interface CheckoutView : BaseView<Pair<String, String>>

class CheckoutPresenter @Inject constructor(private val checkoutUseCase: CheckoutUseCase) :
        BasePresenter<Pair<String, String>, CheckoutView>(arrayOf(checkoutUseCase)) {

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
        SingleUseCase<Pair<String, String>, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<Pair<String, String>> {
        return cartRepository.getCartProductList()
                .first(listOf())
                .flatMap { checkoutRepository.createCheckout(it) }
    }
}