package com.shopify.ui.payment.contract

import com.domain.entity.Card
import com.domain.interactor.base.SingleUseCase
import com.shopify.repository.CheckoutRepository
import com.ui.contract.BasePresenter
import com.ui.contract.BaseView
import io.reactivex.Single
import javax.inject.Inject

interface CardPaymentView : BaseView<Boolean>

class CardPaymentPresenter constructor(private val cardPaymentUseCase: CardPaymentUseCase) :
        BasePresenter<Boolean, CardPaymentView>(arrayOf(cardPaymentUseCase)) {

    fun pay(checkoutId: String, card: Card) {

        cardPaymentUseCase.execute(
                { view?.showContent(it) },
                { it.printStackTrace() },
                CardPaymentUseCase.Params(checkoutId, card)
        )
    }
}

class CardPaymentUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        SingleUseCase<Boolean, CardPaymentUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<Boolean> =
            checkoutRepository.payByCard(params.card)
                    .flatMap { checkoutRepository.completeCheckoutByCard(params.checkoutId, it) }

    class Params(val checkoutId: String, val card: Card)
}