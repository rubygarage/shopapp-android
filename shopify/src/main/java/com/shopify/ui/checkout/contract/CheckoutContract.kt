package com.shopify.ui.checkout.contract

import com.domain.entity.Address
import com.domain.entity.CartProduct
import com.domain.entity.Customer
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.cart.CartItemsUseCase
import com.shopify.entity.Checkout
import com.shopify.interactor.checkout.CreateCheckoutUseCase
import com.shopify.interactor.checkout.GetCheckoutUseCase
import com.shopify.interactor.checkout.SetShippingAddressUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface CheckoutView : BaseLceView<Checkout> {

    fun customerReceived(customer: Customer?)

    fun cartProductListReceived(cartProductList: List<CartProduct>)
}

class CheckoutPresenter @Inject constructor(
    private val cartItemsUseCase: CartItemsUseCase,
    private val createCheckoutUseCase: CreateCheckoutUseCase,
    private val getCheckoutUseCase: GetCheckoutUseCase,
    private val getCustomerUseCase: GetCustomerUseCase,
    private val setShippingAddressUseCase: SetShippingAddressUseCase
) :
    BaseLcePresenter<Checkout, CheckoutView>(
        cartItemsUseCase,
        createCheckoutUseCase,
        getCheckoutUseCase,
        getCustomerUseCase,
        setShippingAddressUseCase
    ) {

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
            { getCustomer(it) },
            { resolveError(it) },
            cartProductList
        )
    }

    private fun getCustomer(checkout: Checkout) {
        getCustomerUseCase.execute(
            {
                view?.customerReceived(it)
                val defaultAddress = it.defaultAddress
                if (defaultAddress != null) {
                    setShippingAddress(checkout, defaultAddress)
                } else {
                    view?.showContent(checkout)
                }
            },
            {
                it.printStackTrace()
                view?.showContent(checkout)
                view?.customerReceived(null)
            },
            Unit
        )
    }

    private fun setShippingAddress(checkout: Checkout, address: Address) {
        setShippingAddressUseCase.execute(
            {
                view?.showContent(it)
            },
            {
                it.printStackTrace()
                view?.showContent(checkout)
            },
            SetShippingAddressUseCase.Params(checkout.checkoutId, address)
        )
    }

    fun getCheckout(checkoutId: String) {
        getCheckoutUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            checkoutId
        )
    }

}