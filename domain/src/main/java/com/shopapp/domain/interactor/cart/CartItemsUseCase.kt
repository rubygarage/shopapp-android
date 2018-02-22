package com.shopapp.domain.interactor.cart

import com.shopapp.gateway.entity.CartProduct
import com.shopapp.domain.interactor.base.ObservableUseCase
import com.shopapp.domain.repository.CartRepository
import io.reactivex.Observable
import javax.inject.Inject

class CartItemsUseCase @Inject constructor(private val cartRepository: CartRepository) :
    ObservableUseCase<List<CartProduct>, Unit>() {

    override fun buildUseCaseObservable(params: Unit): Observable<List<CartProduct>> {
        return cartRepository.getCartProductList()
    }
}