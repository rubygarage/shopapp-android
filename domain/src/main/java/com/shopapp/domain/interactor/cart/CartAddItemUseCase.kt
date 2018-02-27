package com.shopapp.domain.interactor.cart

import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CartRepository
import com.shopapp.gateway.entity.CartProduct
import io.reactivex.Single
import javax.inject.Inject

class CartAddItemUseCase @Inject constructor(private val cartRepository: CartRepository) :
    SingleUseCase<CartProduct, CartProduct>() {

    override fun buildUseCaseSingle(params: CartProduct): Single<CartProduct> {
        return cartRepository.addCartProduct(params)
    }
}