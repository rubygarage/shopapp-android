package com.client.shop.ui.address.checkout.contract

import com.client.shop.ui.address.base.contract.AddressListPresenter
import com.client.shop.ui.address.base.contract.AddressListView
import com.client.shop.getaway.entity.Address
import com.domain.interactor.account.DeleteCustomerAddressUseCase
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.account.SetDefaultAddressUseCase
import com.domain.interactor.checkout.SetShippingAddressUseCase

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