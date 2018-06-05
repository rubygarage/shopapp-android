package com.shopapp.ui.address.checkout.contract

import com.shopapp.R
import com.shopapp.domain.interactor.account.AddCustomerAddressUseCase
import com.shopapp.domain.interactor.account.GetCountriesUseCase
import com.shopapp.domain.interactor.account.UpdateCustomerAddressUseCase
import com.shopapp.domain.interactor.checkout.SetShippingAddressUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.base.contract.AddressPresenter
import com.shopapp.ui.address.base.contract.AddressView

class CheckoutUnAuthAddressPresenter(
    private val fieldValidator: FieldValidator,
    countriesUseCase: GetCountriesUseCase,
    private val setShippingAddressUseCase: SetShippingAddressUseCase,
    addCustomerAddressUseCase: AddCustomerAddressUseCase,
    updateCustomerAddressUseCase: UpdateCustomerAddressUseCase
) :
    AddressPresenter<AddressView>(
        fieldValidator,
        countriesUseCase,
        addCustomerAddressUseCase,
        updateCustomerAddressUseCase,
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