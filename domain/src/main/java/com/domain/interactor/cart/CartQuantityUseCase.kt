package com.domain.interactor.cart

import com.client.shop.gateway.entity.CartProduct
import com.domain.interactor.base.SingleUseCase
import com.domain.repository.CartRepository
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