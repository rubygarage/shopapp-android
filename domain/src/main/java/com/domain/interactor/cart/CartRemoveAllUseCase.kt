package com.domain.interactor.cart

import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.CartRepository
import io.reactivex.Completable
import javax.inject.Inject

class CartRemoveAllUseCase @Inject constructor(private val cartRepository: CartRepository) :
    CompletableUseCase<Unit>() {

    override fun buildUseCaseCompletable(params: Unit): Completable {
        return cartRepository.deleteAllProductsFromCart()
    }
}