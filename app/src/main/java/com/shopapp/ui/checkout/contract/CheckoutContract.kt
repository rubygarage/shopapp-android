package com.shopapp.ui.checkout.contract

import com.shopapp.domain.interactor.cart.CartRemoveAllUseCase
import com.shopapp.domain.interactor.checkout.*
import com.shopapp.gateway.entity.*
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import com.shopapp.ui.const.PaymentType
import javax.inject.Inject

interface CheckoutView : BaseLceView<Checkout> {

    fun customerReceived(customer: Customer?)

    fun cartProductListReceived(cartProductList: List<CartProduct>)

    fun shippingRatesReceived(shippingRates: List<ShippingRate>)

    fun checkoutValidationPassed(isValid: Boolean)

    fun checkoutInProcess()

    fun checkoutCompleted(order: Order)

    fun checkoutError()
}

class CheckoutPresenter @Inject constructor(
    private val setupCheckoutUseCase: SetupCheckoutUseCase,
    private val cartRemoveAllUseCase: CartRemoveAllUseCase,
    private val getCheckoutUseCase: GetCheckoutUseCase,
    private val getShippingRatesUseCase: GetShippingRatesUseCase,
    private val setShippingRateUseCase: SetShippingRateUseCase,
    private val completeCheckoutByCardUseCase: CompleteCheckoutByCardUseCase
) :
    BaseLcePresenter<Checkout, CheckoutView>(
        setupCheckoutUseCase,
        cartRemoveAllUseCase,
        getCheckoutUseCase,
        getShippingRatesUseCase,
        setShippingRateUseCase,
        completeCheckoutByCardUseCase
    ) {

    fun getCheckoutData() {
        setupCheckoutUseCase.execute(
            {
                val (productList, checkout, customer) = it
                view?.cartProductListReceived(productList)
                view?.showContent(checkout)
                view?.customerReceived(customer)
            },
            {
                resolveError(it)
                if (it is Error.NonCritical) {
                    view?.showError(Error.Content())
                }
            },
            Unit
        )
    }

    fun getCheckout(checkoutId: String) {
        getCheckoutUseCase.execute(
            { view?.showContent(it) },
            { resolveError(it) },
            checkoutId
        )
    }

    fun getShippingRates(checkoutId: String) {
        getShippingRatesUseCase.execute(
            { view?.shippingRatesReceived(it) },
            { it.printStackTrace() },
            checkoutId
        )
    }

    fun setShippingRates(checkout: Checkout, shippingRate: ShippingRate) {
        setShippingRateUseCase.execute(
            { view?.showContent(it) },
            { view?.showContent(checkout) },
            SetShippingRateUseCase.Params(checkout.checkoutId, shippingRate)
        )
    }

    fun verifyCheckoutData(
        checkout: Checkout?,
        shippingAddress: Address?,
        email: String?,
        paymentType: PaymentType?,
        card: Card?,
        cardToken: String?,
        billingAddress: Address?
    ) {
        if (checkout != null) {
            if (checkout.shippingRate == null) {
                view?.checkoutValidationPassed(false)
                return
            }
            if (email == null) {
                view?.checkoutValidationPassed(false)
                return
            }
            if (shippingAddress == null) {
                view?.checkoutValidationPassed(false)
                return
            }
            if (paymentType == null) {
                view?.checkoutValidationPassed(false)
                return
            } else if (paymentType == PaymentType.CARD_PAYMENT
                && (card == null || cardToken == null || billingAddress == null)) {
                view?.checkoutValidationPassed(false)
                return
            }
            view?.checkoutValidationPassed(true)
        } else {
            view?.checkoutValidationPassed(false)
        }
    }

    fun completeCheckoutByCard(
        checkout: Checkout?,
        email: String?,
        billingAddress: Address?,
        cardToken: String?
    ) {
        if (checkout != null && email != null && billingAddress != null && cardToken != null) {
            view?.checkoutInProcess()
            completeCheckoutByCardUseCase.execute(
                {
                    cartRemoveAllUseCase.execute({}, {}, Unit)
                    view?.checkoutCompleted(it)
                },
                {
                    view?.checkoutError()
                },
                CompleteCheckoutByCardUseCase.Params(checkout, email, billingAddress, cardToken)
            )
        }
    }
}