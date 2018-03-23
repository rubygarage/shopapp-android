package com.shopapp.ui.address.checkout.contract

import com.shopapp.R
import com.shopapp.domain.interactor.account.CreateCustomerAddressUseCase
import com.shopapp.domain.interactor.account.EditCustomerAddressUseCase
import com.shopapp.domain.interactor.account.GetCountriesUseCase
import com.shopapp.domain.interactor.checkout.SetShippingAddressUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView

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
}