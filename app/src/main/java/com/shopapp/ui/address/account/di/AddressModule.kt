package com.client.shop.ui.address.account.di

import com.client.shop.ui.address.base.contract.AddressListPresenter
import com.client.shop.ui.address.base.contract.AddressListView
import com.client.shop.ui.address.base.contract.AddressPresenter
import com.client.shop.ui.address.base.contract.AddressView
import com.domain.interactor.account.*
import com.domain.validator.FieldValidator
import dagger.Module
import dagger.Provides

@Module
class AddressModule {

    @Provides
    fun provideAddressPresenter(
        formValidator: FieldValidator,
        countriesUseCase: GetCountriesUseCase,
        createCustomerAddressUseCase: CreateCustomerAddressUseCase,
        editCustomerAddressUseCase: EditCustomerAddressUseCase
    ): AddressPresenter<AddressView> = AddressPresenter(
        formValidator,
        countriesUseCase,
        createCustomerAddressUseCase,
        editCustomerAddressUseCase
    )

    @Provides
    fun provideAddressListPresenter(
        getCustomerUseCase: GetCustomerUseCase,
        deleteCustomerAddressUseCase: DeleteCustomerAddressUseCase,
        setDefaultAddressUseCase: SetDefaultAddressUseCase
    ) = AddressListPresenter<AddressListView>(
        getCustomerUseCase,
        deleteCustomerAddressUseCase,
        setDefaultAddressUseCase
    )
}