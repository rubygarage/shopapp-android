package com.domain.interactor.cart

import com.client.shop.getaway.entity.CartProduct
import com.domain.interactor.base.ObservableUseCase
import com.domain.repository.CartRepository
import io.reactivex.Observable
import javax.inject.Inject

class CartItemsUseCase @Inject constructor(private val cartRepository: CartRepository) :
    ObservableUseCase<List<CartProduct>, Unit>() {

    override fun buildUseCaseObservable(params: Unit): Observable<List<CartProduct>> {
        return cartRepository.getCartProductList()
    }
}