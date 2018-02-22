package com.shopapp.ui.address.checkout.contract

import com.shopapp.gateway.entity.Address
import com.shopapp.ui.address.base.contract.AddressListPresenter
import com.shopapp.ui.address.base.contract.AddressListView
import com.shopapp.domain.interactor.account.DeleteCustomerAddressUseCase
import com.shopapp.domain.interactor.account.GetCustomerUseCase
import com.shopapp.domain.interactor.account.SetDefaultAddressUseCase
import com.shopapp.domain.interactor.checkout.SetShippingAddressUseCase

interface CheckoutAddressListView : AddressListView {

    fun selectedAddressChanged(address: Address)
}

class CheckoutAddressListPresenter(
    getCustomerUseCase: GetCustomerUseCase,
    deleteCustomerAddressUseCase: DeleteCustomerAddressUseCase,
    setDefaultAddressUseCase: SetDefaultAddressUseCase,
    private val setShippingAddressUseCase: SetShippingAddressUseCase
) :
    AddressListPresenter<CheckoutAddressListView>(
        getCustomerUseCase,
        deleteCustomerAddressUseCase,
        setDefaultAddressUseCase,
        setShippingAddressUseCase
    ) {

    fun setShippingAddress(checkoutId: String, address: Address) {
        setShippingAddressUseCase.execute(
            { view?.selectedAddressChanged(address) },
            { resolveError(it) },
            SetShippingAddressUseCase.Params(checkoutId, address)
        )
    }
}