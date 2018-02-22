package com.shopapp.ui.cart.contract

import com.shopapp.gateway.entity.CartProduct
import com.shopapp.domain.interactor.cart.CartItemsUseCase
import com.shopapp.domain.interactor.cart.CartQuantityUseCase
import com.shopapp.domain.interactor.cart.CartRemoveUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
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