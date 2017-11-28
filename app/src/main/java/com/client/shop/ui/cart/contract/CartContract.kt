package com.client.shop.ui.cart.contract

import com.domain.entity.CartProduct
import com.domain.interactor.base.CompletableUseCase
import com.domain.interactor.base.SingleUseCase
import com.repository.CartRepository
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

interface CartView : BaseView<List<CartProduct>>

class CartPresenter @Inject constructor(
        private val cartItemsUseCase: CartItemsUseCase,
        private val cartRemoveUseCase: CartRemoveUseCase,
        private val cartQuantityUseCase: CartQuantityUseCase
) :
        BasePresenter<List<CartProduct>, CartView>(arrayOf(cartItemsUseCase, cartRemoveUseCase, cartQuantityUseCase)) {

    fun loadCartItems() {

        cartItemsUseCase.execute(
                {
                    if (it.isNotEmpty()) {
                        view?.showContent(it)
                    } else {
                        view?.showEmptyState()
                    }
                },
                { it.printStackTrace() },
                { },
                Unit
        )
    }

    fun removeProduct(productVariantId: String) {

        cartRemoveUseCase.execute(
                { },
                { it.printStackTrace() },
                productVariantId
        )
    }

    fun changeProductQuantity(productVariantId: String, newQuantity: Int) {

        cartQuantityUseCase.execute(
                { },
                { it.printStackTrace() },
                CartQuantityUseCase.Params(productVariantId, newQuantity)
        )
    }
}

class CartRemoveUseCase @Inject constructor(private val cartRepository: CartRepository) :
        CompletableUseCase<String>() {

    override fun buildUseCaseCompletable(params: String): Completable {
        return cartRepository.deleteProductFromCart(params)
    }
}

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