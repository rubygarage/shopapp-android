package com.ui.module.address.contract

import com.domain.entity.Address
import com.domain.interactor.account.DeleteCustomerAddressUseCase
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.account.SetDefaultAddressUseCase
import com.domain.interactor.base.UseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView

interface AddressListView : BaseLceView<Pair<Address?, List<Address>>> {

    fun defaultAddressChanged(address: Address)
}

open class AddressListPresenter<V : AddressListView>(
    private val getCustomerUseCase: GetCustomerUseCase,
    private val deleteCustomerAddressUseCase: DeleteCustomerAddressUseCase,
    private val setDefaultAddressUseCase: SetDefaultAddressUseCase,
    vararg useCases: UseCase
) :
    BaseLcePresenter<Pair<Address?, List<Address>>, V>(
        getCustomerUseCase,
        deleteCustomerAddressUseCase,
        setDefaultAddressUseCase,
        *useCases
    ) {

    fun getAddressList() {
        getCustomerUseCase.execute(
            { view?.showContent(Pair(it.defaultAddress, it.addressList)) },
            { view?.showContent(Pair(null, emptyList())) },
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

    fun setDefaultAddress(address: Address) {
        setDefaultAddressUseCase.execute(
            { view?.defaultAddressChanged(address) },
            { resolveError(it) },
            address.id
        )
    }
}