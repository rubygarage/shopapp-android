package com.shopify.ui.checkout.contract

import com.domain.entity.CartProduct
import com.domain.interactor.cart.CartItemsUseCase
import com.shopify.entity.Checkout
import com.shopify.interactor.checkout.CreateCheckoutUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface CheckoutView : BaseLceView<Checkout> {

    fun cartProductListReceived(cartProductList: List<CartProduct>)
}

class CheckoutPresenter @Inject constructor(
        private val cartItemsUseCase: CartItemsUseCase,
        private val createCheckoutUseCase: CreateCheckoutUseCase
) :
        BaseLcePresenter<Checkout, CheckoutView>(cartItemsUseCase, createCheckoutUseCase) {

    fun getCartProductList() {

        cartItemsUseCase.execute(
                {
                    view?.cartProductListReceived(it)
                    createCheckout(it)
                },
                { resolveError(it) },
                {},
                Unit
        )
    }

    private fun createCheckout(cartProductList: List<CartProduct>) {

        createCheckoutUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                cartProductList
        )
    }
}