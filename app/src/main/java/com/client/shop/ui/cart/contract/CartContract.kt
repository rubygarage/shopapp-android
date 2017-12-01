package com.client.shop.ui.cart.contract

import com.domain.entity.CartProduct
import com.domain.interactor.cart.CartItemsUseCase
import com.domain.interactor.cart.CartQuantityUseCase
import com.domain.interactor.cart.CartRemoveUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface CartView : BaseLceView<List<CartProduct>>

class CartPresenter @Inject constructor(
        private val cartItemsUseCase: CartItemsUseCase,
        private val cartRemoveUseCase: CartRemoveUseCase,
        private val cartQuantityUseCase: CartQuantityUseCase
) :
        BaseLcePresenter<List<CartProduct>, CartView>(cartItemsUseCase, cartRemoveUseCase, cartQuantityUseCase) {

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