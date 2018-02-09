package com.client.shop.ui.address.checkout.contract

import com.client.shop.R
import com.client.shop.ui.address.base.contract.AddressPresenter
import com.client.shop.ui.address.base.contract.AddressView
import com.client.shop.getaway.entity.Address
import com.domain.interactor.account.CreateCustomerAddressUseCase
import com.domain.interactor.account.EditCustomerAddressUseCase
import com.domain.interactor.account.GetCountriesUseCase
import com.domain.interactor.checkout.SetShippingAddressUseCase
import com.domain.validator.FieldValidator

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