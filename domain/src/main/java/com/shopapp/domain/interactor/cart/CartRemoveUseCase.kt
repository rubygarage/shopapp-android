package com.domain.interactor.cart

import com.domain.interactor.base.CompletableUseCase
import com.domain.repository.CartRepository
import io.reactivex.Completable
import javax.inject.Inject

class CartRemoveUseCase @Inject constructor(private val cartRepository: CartRepository) :
    CompletableUseCase<String>() {

    override fun buildUseCaseCompletable(params: String): Completable {
        return cartRepository.deleteProductFromCart(params)
    }
}