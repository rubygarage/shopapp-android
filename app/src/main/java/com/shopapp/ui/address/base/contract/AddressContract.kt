package com.shopapp.ui.address.base.contract

import com.shopapp.R
import com.shopapp.domain.interactor.account.AddCustomerAddressUseCase
import com.shopapp.domain.interactor.account.GetCountriesUseCase
import com.shopapp.domain.interactor.account.UpdateCustomerAddressUseCase
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
        private val addCustomerAddressUseCase: AddCustomerAddressUseCase,
        private val updateCustomerAddressUseCase: UpdateCustomerAddressUseCase,
        vararg useCases: UseCase
) :
        BaseLcePresenter<Address?, V>(
                countriesUseCase,
                addCustomerAddressUseCase,
                updateCustomerAddressUseCase,
                *useCases
        ) {

    fun submitAddress(address: Address) {
        if (fieldValidator.isAddressValid(address)) {
            addCustomerAddress(address)
        } else {
            view?.submitAddressError()
            view?.showMessage(R.string.invalid_address)
        }
    }

    fun updateAddress(address: Address) {
        if (fieldValidator.isAddressValid(address)) {
            updateCustomerAddress(address)
        } else {
            view?.submitAddressError()
            view?.showMessage(R.string.invalid_address)
        }
    }

    fun getCountries() {
        countriesUseCase.execute(
                { view.countriesLoaded(it) },
                { resolveError(it) },
                Unit
        )
    }

    private fun addCustomerAddress(address: Address) {
        addCustomerAddressUseCase.execute(
                { view?.addressChanged(address) },
                {
                    resolveError(it)
                    view?.addressChanged(address)
                },
                address
        )
    }

    private fun updateCustomerAddress(address: Address) {
        updateCustomerAddressUseCase.execute(
                { view?.addressChanged(address) },
                {
                    resolveError(it)
                    view?.addressChanged(address)
                },
                UpdateCustomerAddressUseCase.Params(address)
        )
    }
}