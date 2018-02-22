package com.shopapp.domain.interactor.checkout

import com.shopapp.gateway.entity.CardType
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class GetAcceptedCardTypesUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<List<CardType>, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<List<CardType>> {
        return checkoutRepository.getAcceptedCardTypes()
    }
}