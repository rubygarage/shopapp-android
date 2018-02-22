package com.shopapp.domain.interactor.cart

import com.shopapp.domain.interactor.base.CompletableUseCase
import com.shopapp.domain.repository.CartRepository
import io.reactivex.Completable
import javax.inject.Inject

class CartRemoveUseCase @Inject constructor(private val cartRepository: CartRepository) :
    CompletableUseCase<String>() {

    override fun buildUseCaseCompletable(params: String): Completable {
        return cartRepository.deleteProductFromCart(params)
    }
}