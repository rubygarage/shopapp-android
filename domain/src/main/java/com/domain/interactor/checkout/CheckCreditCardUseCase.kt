package com.domain.interactor.checkout

import com.client.shop.getaway.entity.Card
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class CheckCreditCardUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<String, Card>() {

    override fun buildUseCaseSingle(params: Card): Single<String> {
        return checkoutRepository.getCardToken(params)
    }
}