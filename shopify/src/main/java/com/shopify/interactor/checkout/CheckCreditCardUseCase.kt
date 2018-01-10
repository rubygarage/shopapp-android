package com.shopify.interactor.checkout

import com.domain.entity.Card
import com.domain.interactor.base.SingleUseCase
import com.shopify.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class CheckCreditCardUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
        SingleUseCase<String, Card>() {

    override fun buildUseCaseSingle(params: Card): Single<String> {
        return checkoutRepository.payByCard(params)
    }
}