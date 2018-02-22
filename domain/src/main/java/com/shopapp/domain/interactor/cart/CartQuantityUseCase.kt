package com.shopapp.domain.interactor.cart

import com.shopapp.gateway.entity.CartProduct
import com.shopapp.domain.interactor.base.SingleUseCase
import com.shopapp.domain.repository.CartRepository
import io.reactivex.Single
import javax.inject.Inject

class CartQuantityUseCase @Inject constructor(private val cartRepository: CartRepository) :
    SingleUseCase<CartProduct, CartQuantityUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<CartProduct> {
        return with(params) {
            cartRepository.changeCartProductQuantity(productVariantId, newQuantity)
        }
    }

    data class Params(
        val productVariantId: String,
        val newQuantity: Int
    )
}