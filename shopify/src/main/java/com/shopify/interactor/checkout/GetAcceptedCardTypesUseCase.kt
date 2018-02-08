package com.shopify.interactor.checkout

import com.domain.entity.CardType
import com.domain.interactor.base.SingleUseCase
import com.shopify.repository.CheckoutRepository
import io.reactivex.Single
import javax.inject.Inject

class GetAcceptedCardTypesUseCase @Inject constructor(private val checkoutRepository: CheckoutRepository) :
    SingleUseCase<List<CardType>, Unit>() {

    override fun buildUseCaseSingle(params: Unit): Single<List<CardType>> {
        return checkoutRepository.getAcceptedCardTypes()
    }
}