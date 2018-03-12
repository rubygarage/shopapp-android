package com.shopapp.ui.address.base.contract

import com.shopapp.R
import com.shopapp.domain.interactor.account.CreateCustomerAddressUseCase
import com.shopapp.domain.interactor.account.EditCustomerAddressUseCase
import com.shopapp.domain.interactor.account.GetCountriesUseCase
import com.shopapp.domain.interactor.base.UseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView

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
            { view.countriesLoaded(it) },
            { resolveError(it) },
            Unit
        )
    }

    private fun createCustomerAddress(address: Address) {
        createCustomerAddressUseCase.execute(
            { view?.addressChanged(address) },
            {
                resolveError(it)
                view?.addressChanged(address)
            },
            address
        )
    }

    private fun editCustomerAddress(addressId: String, address: Address) {
        editCustomerAddressUseCase.execute(
            { view?.addressChanged(address) },
            {
                resolveError(it)
                view?.addressChanged(address)
            },
            EditCustomerAddressUseCase.Params(addressId, address)
        )
    }
}