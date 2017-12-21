package com.shopify.ui.checkout.contract

import com.domain.entity.CartProduct
import com.domain.interactor.cart.CartItemsUseCase
import com.shopify.entity.Checkout
import com.shopify.interactor.checkout.CheckoutUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface CheckoutView : BaseLceView<Checkout> {

    fun cartProductListReceived(cartProductList: List<CartProduct>)
}

class CheckoutPresenter @Inject constructor(
        private val cartItemsUseCase: CartItemsUseCase,
        private val checkoutUseCase: CheckoutUseCase
) :
        BaseLcePresenter<Checkout, CheckoutView>(cartItemsUseCase, checkoutUseCase) {

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

        checkoutUseCase.execute(
                { view?.showContent(it) },
                { resolveError(it) },
                cartProductList
        )
    }
}