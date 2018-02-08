package com.shopify.ui.address.contract

import com.domain.entity.Address
import com.domain.interactor.account.DeleteCustomerAddressUseCase
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.account.SetDefaultAddressUseCase
import com.shopify.interactor.checkout.SetShippingAddressUseCase
import com.ui.module.address.contract.AddressListPresenter
import com.ui.module.address.contract.AddressListView

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