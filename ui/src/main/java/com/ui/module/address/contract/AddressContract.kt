package com.ui.module.address.contract

import com.domain.entity.Address
import com.domain.entity.Country
import com.domain.interactor.account.CreateCustomerAddressUseCase
import com.domain.interactor.account.EditCustomerAddressUseCase
import com.domain.interactor.account.GetCountriesUseCase
import com.domain.interactor.base.UseCase
import com.domain.validator.FieldValidator
import com.ui.R
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView

interface AddressView : BaseLceView<Address?> {

    fun addressChanged(address: Address)

    fun submitAddressError()

    fun countriesLoaded(countries: List<Country>)
}

open class AddressPresenter<V : AddressView>(
    private val fieldValidator: FieldValidator,
    private val countriesUseCase: GetCountriesUseCase,
    private val createCustomerAddressUseCase: CreateCustomerAddressUseCase,
    private val editCustomerAddressUseCase: EditCustomerAddressUseCase,
    vararg useCases: UseCase
) :
    BaseLcePresenter<Address?, V>(
        countriesUseCase,
        createCustomerAddressUseCase,
        editCustomerAddressUseCase,
        *useCases
    ) {

    fun submitAddress(address: Address) {
        if (fieldValidator.isAddressValid(address)) {
            createCustomerAddress(address)
        } else {
            view?.submitAddressError()
            view?.showMessage(R.string.invalid_address)
        }
    }

    fun editAddress(addressId: String, address: Address) {
        if (fieldValidator.isAddressValid(address)) {
            editCustomerAddress(addressId, address)
        } else {
            view?.submitAddressError()
            view?.showMessage(R.string.invalid_address)
        }
    }

    fun getCountriesList() {
        countriesUseCase.execute(
            {view.countriesLoaded(it)},
            {},
            Unit
        )
    }

    private fun createCustomerAddress(address: Address) {
        createCustomerAddressUseCase.execute(
            { view?.addressChanged(address) },
            {
                it.printStackTrace()
                view?.addressChanged(address)
            },
            CreateCustomerAddressUseCase.Params(address)
        )
    }

    private fun editCustomerAddress(addressId: String, address: Address) {
        editCustomerAddressUseCase.execute(
            { view?.addressChanged(address) },
            {
                it.printStackTrace()
                view?.addressChanged(address)
            },
            EditCustomerAddressUseCase.Params(addressId, address)
        )
    }
}