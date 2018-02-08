package com.shopify.ui.address.contract

import com.domain.entity.Address
import com.domain.interactor.account.CreateCustomerAddressUseCase
import com.domain.interactor.account.EditCustomerAddressUseCase
import com.domain.interactor.account.GetCountriesUseCase
import com.domain.validator.FieldValidator
import com.shopify.api.R
import com.shopify.interactor.checkout.SetShippingAddressUseCase
import com.ui.module.address.contract.AddressPresenter
import com.ui.module.address.contract.AddressView

class CheckoutUnAuthAddressPresenter(
    private val fieldValidator: FieldValidator,
    countriesUseCase: GetCountriesUseCase,
    private val setShippingAddressUseCase: SetShippingAddressUseCase,
    createCustomerAddressUseCase: CreateCustomerAddressUseCase,
    editCustomerAddressUseCase: EditCustomerAddressUseCase
) :
    AddressPresenter<AddressView>(
        fieldValidator,
        countriesUseCase,
        createCustomerAddressUseCase,
        editCustomerAddressUseCase,
        setShippingAddressUseCase
    ) {

    fun submitShippingAddress(checkoutId: String, address: Address) {
        if (fieldValidator.isAddressValid(address)) {
            setShippingAddressUseCase.execute(
                {
                    view?.addressChanged(address)
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

    fun editShippingAddress(checkoutId: String, address: Address) {
        if (fieldValidator.isAddressValid(address)) {
            setShippingAddressUseCase.execute(
                {
                    view?.addressChanged(address)
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
}