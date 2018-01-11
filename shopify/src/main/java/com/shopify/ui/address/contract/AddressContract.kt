package com.shopify.ui.address.contract

import com.domain.entity.Address
import com.domain.interactor.account.CreateCustomerAddressUseCase
import com.domain.interactor.account.EditCustomerAddressUseCase
import com.domain.interactor.account.SessionCheckUseCase
import com.domain.validator.FieldValidator
import com.shopify.api.R
import com.shopify.interactor.checkout.GetCheckoutUseCase
import com.shopify.interactor.checkout.SetShippingAddressUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView

interface AddressView : BaseLceView<Address?> {

    fun sessionChecked(isLoggedIn: Boolean)

    fun addressChanged(address: Address)

    fun submitAddressError()
}

class AddressPresenter(
        private val fieldValidator: FieldValidator,
        private val getCheckoutUseCase: GetCheckoutUseCase,
        private val setShippingAddressUseCase: SetShippingAddressUseCase,
        private val sessionCheckUseCase: SessionCheckUseCase,
        private val createCustomerAddressUseCase: CreateCustomerAddressUseCase,
        private val editCustomerAddressUseCase: EditCustomerAddressUseCase
) :
        BaseLcePresenter<Address?, AddressView>(
                getCheckoutUseCase,
                setShippingAddressUseCase,
                sessionCheckUseCase,
                createCustomerAddressUseCase,
                editCustomerAddressUseCase
        ) {

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

    fun submitAddress(checkoutId: String?, address: Address, isDefault: Boolean) {
        if (fieldValidator.isAddressValid(address)) {
            if (checkoutId != null) {
                setShippingAddressUseCase.execute(
                        {
                            if (isLoggedIn) {
                                createCustomerAddress(address, isDefault)
                            } else {
                                view?.addressChanged(address)
                            }
                        },
                        {
                            resolveError(it)
                            view?.submitAddressError()
                        },
                        SetShippingAddressUseCase.Params(checkoutId, address)
                )
            } else {
                createCustomerAddress(address, isDefault)
            }
        } else {
            view?.submitAddressError()
            view?.showMessage(R.string.invalid_address)
        }
    }

    fun editAddress(checkoutId: String?, addressId: String, address: Address, isDefault: Boolean) {
        if (fieldValidator.isAddressValid(address)) {
            if (checkoutId != null) {
                setShippingAddressUseCase.execute(
                        {
                            editCustomerAddress(addressId, address, isDefault)
                        },
                        {
                            resolveError(it)
                            view?.submitAddressError()
                        },
                        SetShippingAddressUseCase.Params(checkoutId, address)
                )
            } else {
                editCustomerAddress(addressId, address, isDefault)
            }
        } else {
            view?.submitAddressError()
            view?.showMessage(R.string.invalid_address)
        }
    }

    private fun createCustomerAddress(address: Address, isDefault: Boolean) {
        createCustomerAddressUseCase.execute(
                { view?.addressChanged(address) },
                {
                    it.printStackTrace()
                    view?.addressChanged(address)
                },
                CreateCustomerAddressUseCase.Params(address, isDefault)
        )
    }

    private fun editCustomerAddress(addressId: String, address: Address, isDefault: Boolean) {
        editCustomerAddressUseCase.execute(
                { view?.addressChanged(address) },
                {
                    it.printStackTrace()
                    view?.addressChanged(address)
                },
                EditCustomerAddressUseCase.Params(addressId, address, isDefault)
        )
    }
}