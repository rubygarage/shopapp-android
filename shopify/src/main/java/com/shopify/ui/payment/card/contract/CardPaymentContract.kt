package com.shopify.ui.payment.card.contract

import com.domain.entity.Address
import com.domain.entity.Card
import com.domain.interactor.base.SingleUseCase
import com.shopify.entity.Checkout
import com.shopify.repository.CheckoutRepository
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import io.reactivex.Single
import javax.inject.Inject

interface CardPaymentView : BaseView<Boolean>

class CardPaymentPresenter constructor(private val cardPaymentUseCase: CardPaymentUseCase) :
        BasePresenter<Boolean, CardPaymentView>(arrayOf(cardPaymentUseCase)) {

    fun pay(checkout: Checkout, card: Card, address: Address) {
        cardPaymentUseCase.execute(
                { view?.showContent(it) },
                { it.printStackTrace() },
                CardPaymentUseCase.Params(checkout, card, address)
        )
    }
}

class CardPaymentUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        SingleUseCase<Boolean, CardPaymentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Boolean> =
            checkoutRepository.payByCard(params.card)
                    .flatMap { checkoutRepository.completeCheckoutByCard(params.checkout, params.address, it) }

    class Params(
            val checkout: Checkout,
            val card: Card,
            val address: Address
    )
}