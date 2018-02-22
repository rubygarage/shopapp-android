package com.shopapp.domain.interactor.checkout

import com.shopapp.gateway.entity.Card
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class CheckCreditCardUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<String, Card>() {

    override fun buildUseCaseSingle(params: Card): Single<String> {
        return checkoutRepository.getCardToken(params)
    }
}