package com.ui.module.address.contract

import com.domain.entity.Address
import com.domain.interactor.account.DeleteCustomerAddressUseCase
import com.domain.interactor.account.GetCustomerUseCase
import com.domain.interactor.account.SetDefaultAddressUseCase
import com.domain.interactor.base.UseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import com.ui.ext.swap

interface AddressListView : BaseLceView<Pair<Address?, List<Address>>>

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
            {
                val defaultAddress = it.defaultAddress
                val addressList = sortAddressList(defaultAddress, it.addressList)
                view?.showContent(Pair(defaultAddress, addressList))
            },
            {
                it?.let {
                    view?.showContent(Pair(it.defaultAddress, it.addressList))
                }
            },
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

    fun setDefaultAddress(address: Address, addressList: List<Address>) {
        setDefaultAddressUseCase.execute(
            {
                val sortedList = sortAddressList(address, addressList)
                view?.showContent(Pair(address, sortedList))
            },
            { resolveError(it) },
            address.id
        )
    }

    private fun sortAddressList(defaultAddress: Address?, addressList: List<Address>): List<Address> {
        val mutableAddressList = addressList.toMutableList()
        defaultAddress?.let {
            val defaultIndex = addressList.indexOf(it)
            if (defaultIndex >= 0) {
                mutableAddressList.swap(defaultIndex, 0)
            }
        }
        return mutableAddressList
    }
}