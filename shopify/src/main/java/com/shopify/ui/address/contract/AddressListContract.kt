package com.shopify.ui.address.contract

import com.domain.entity.Address
import com.domain.interactor.account.DeleteCustomerAddressUseCase
import com.domain.interactor.account.GetCustomerUseCase
import com.shopify.interactor.checkout.SetShippingAddressUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView

interface AddressListView : BaseLceView<List<Address>> {

    fun selectedAddressChanged(address: Address)
}

class AddressListPresenter(
        private val getCustomerUseCase: GetCustomerUseCase,
        private val deleteCustomerAddressUseCase: DeleteCustomerAddressUseCase,
        private val setShippingAddressUseCase: SetShippingAddressUseCase
) :
        BaseLcePresenter<List<Address>, AddressListView>(
                getCustomerUseCase,
                deleteCustomerAddressUseCase,
                setShippingAddressUseCase
        ) {

    fun getAddressList() {
        getCustomerUseCase.execute(
                { view?.showContent(it.addressList) },
                { view?.showContent(emptyList()) },
                Unit
        )
    }

    fun deleteAddress(addressId: String) {
        deleteCustomerAddressUseCase.execute(
                {},
                { it.printStackTrace() },
                addressId
        )
    }

    fun setShippingAddress(checkoutId: String, address: Address) {
        setShippingAddressUseCase.execute(
                { view?.selectedAddressChanged(address) },
                { resolveError(it) },
                SetShippingAddressUseCase.Params(checkoutId, address)
        )
    }
}