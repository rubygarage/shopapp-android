package com.shopify.ui.address.contract

import com.domain.entity.Address
import com.domain.interactor.account.CreateCustomerAddressUseCase
import com.domain.interactor.account.SessionCheckUseCase
import com.domain.validator.FieldValidator
import com.shopify.api.R
import com.shopify.interactor.checkout.GetCheckoutUseCase
import com.shopify.interactor.checkout.SetShippingAddressUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView

interface AddressView : BaseLceView<Address?> {

    fun sessionChecked(isLoggedIn: Boolean)

    fun addressChanged()

    fun submitAddressError()
}

class AddressPresenter(
        private val getCheckoutUseCase: GetCheckoutUseCase,
        private val setShippingAddressUseCase: SetShippingAddressUseCase,
        private val sessionCheckUseCase: SessionCheckUseCase,
        private val createCustomerAddressUseCase: CreateCustomerAddressUseCase
) :
        BaseLcePresenter<Address?, AddressView>(
                getCheckoutUseCase,
                setShippingAddressUseCase,
                sessionCheckUseCase,
                createCustomerAddressUseCase
        ) {

    private val fieldValidator = FieldValidator()
    private var isLoggedIn = false

    fun checkIsLoggedIn() {
        sessionCheckUseCase.execute(
                {
                    isLoggedIn = it
                    view?.sessionChecked(it)
                },
                { view?.sessionChecked(false) },
                Unit
        )
    }

    fun getAddressFromCheckout(checkoutId: String) {
        getCheckoutUseCase.execute(
                { view?.showContent(it.address) },
                { resolveError(it) },
                checkoutId
        )
    }

    fun submitAddress(checkoutId: String, address: Address, isDefault: Boolean) {
        if (fieldValidator.isAddressValid(address)) {
            setShippingAddressUseCase.execute(
                    {
                        if (isLoggedIn) {
                            createAddress(address, isDefault)
                        } else {
                            view?.addressChanged()
                        }
                    },
                    {
                        resolveError(it)
                        view?.submitAddressError()
                    },
                    SetShippingAddressUseCase.Params(checkoutId, address)
            )
        } else {
            view?.submitAddressError()
            view?.showMessage(R.string.invalid_address)
        }
    }

    private fun createAddress(address: Address, isDefault: Boolean) {
        createCustomerAddressUseCase.execute(
                {
                    view?.addressChanged()
                },
                {
                    it.printStackTrace()
                    view?.addressChanged()
                },
                CreateCustomerAddressUseCase.Params(address, isDefault)
        )
    }
}