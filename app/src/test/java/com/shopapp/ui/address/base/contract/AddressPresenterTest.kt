package com.shopapp.ui.address.base.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.domain.interactor.account.AddCustomerAddressUseCase
import com.shopapp.domain.interactor.account.GetCountriesUseCase
import com.shopapp.domain.interactor.account.UpdateCustomerAddressUseCase
import com.shopapp.domain.validator.FieldValidator
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
import com.shopapp.gateway.entity.Error
import com.shopapp.test.MockInstantiator
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class AddressPresenterTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: AddressView

    @Mock
    private lateinit var fieldValidator: FieldValidator

    @Mock
    private lateinit var countriesUseCase: GetCountriesUseCase

    @Mock
    private lateinit var addCustomerAddressUseCase: AddCustomerAddressUseCase

    @Mock
    private lateinit var updateCustomerAddressUseCase: UpdateCustomerAddressUseCase

    @Mock
    private lateinit var address: Address

    @Mock
    private lateinit var countryList: List<Country>

    private lateinit var presenter: AddressPresenter<AddressView>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = AddressPresenter(fieldValidator, countriesUseCase, addCustomerAddressUseCase,
            updateCustomerAddressUseCase)
        presenter.attachView(view)

        countriesUseCase.mock()
        addCustomerAddressUseCase.mock()
        updateCustomerAddressUseCase.mock()
    }

    @Test
    fun submitAddressShouldShowErrorWhenSubmitInvalidAddress() {
        given(fieldValidator.isAddressValid(any())).willReturn(false)
        presenter.submitAddress(address)

        val inOrder = inOrder(view)
        inOrder.verify(view).submitAddressError()
        inOrder.verify(view).showMessage(R.string.invalid_address)
    }

    @Test
    fun submitAddressShouldCallAddressChangedWhenOnSingleSuccess() {
        val result = "result"
        given(addCustomerAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.just(result))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitAddress(address)

        val inOrder = inOrder(view, addCustomerAddressUseCase)
        inOrder.verify(addCustomerAddressUseCase).execute(any(), any(), eq(address))
        inOrder.verify(view).addressChanged(address)
    }

    @Test
    fun submitAddressShouldCallAddressChangedWhenOnSingleContentError() {
        val error = Error.Content()
        given(addCustomerAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitAddress(address)

        argumentCaptor<Error>().apply {
            val inOrder = inOrder(view, addCustomerAddressUseCase)
            inOrder.verify(addCustomerAddressUseCase).execute(any(), any(), eq(address))
            inOrder.verify(view).showError(capture())
            inOrder.verify(view).addressChanged(address)

            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun submitAddressShouldCallAddressChangedWhenOnSingleNonCriticalError() {
        val message = "message"
        val error = Error.NonCritical(message)
        given(addCustomerAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitAddress(address)

        val inOrder = inOrder(view, addCustomerAddressUseCase)
        inOrder.verify(addCustomerAddressUseCase).execute(any(), any(), eq(address))
        inOrder.verify(view).showMessage(message)
        inOrder.verify(view).addressChanged(address)
    }

    @Test
    fun editAddressShouldShowErrorWhenEditInvalidAddress() {
        given(fieldValidator.isAddressValid(any())).willReturn(false)
        presenter.updateAddress(address)

        val inOrder = inOrder(view)
        inOrder.verify(view).submitAddressError()
        inOrder.verify(view).showMessage(R.string.invalid_address)
    }

    @Test
    fun editAddressShouldCallAddressChangedWhenOnCompletableSuccess() {
        given(updateCustomerAddressUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.updateAddress(address)

        argumentCaptor<UpdateCustomerAddressUseCase.Params>().apply {
            val inOrder = inOrder(view, updateCustomerAddressUseCase)
            inOrder.verify(updateCustomerAddressUseCase).execute(any(), any(), capture())
            inOrder.verify(view).addressChanged(address)

            assertEquals(address, firstValue.address)
        }
    }

    @Test
    fun editAddressShouldCallAddressChangedWhenOnCompletableContentError() {
        val error = Error.Content()
        given(updateCustomerAddressUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.updateAddress(address)


        val inOrder = inOrder(view, updateCustomerAddressUseCase)
        argumentCaptor<UpdateCustomerAddressUseCase.Params>().apply {
            inOrder.verify(updateCustomerAddressUseCase).execute(any(), any(), capture())
            assertEquals(address, firstValue.address)
        }
        argumentCaptor<Error>().apply {
            inOrder.verify(view).showError(capture())
            assertTrue(firstValue is Error.Content)
        }
        inOrder.verify(view).addressChanged(address)
    }

    @Test
    fun editAddressShouldCallAddressChangedWhenOnCompletableNonCriticalError() {
        val message = "message"
        val error = Error.NonCritical(message)
        given(updateCustomerAddressUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.updateAddress(address)

        argumentCaptor<UpdateCustomerAddressUseCase.Params>().apply {
            val inOrder = inOrder(view, updateCustomerAddressUseCase)
            inOrder.verify(updateCustomerAddressUseCase).execute(any(), any(), capture())
            inOrder.verify(view).showMessage(message)
            inOrder.verify(view).addressChanged(address)

            assertEquals(address, firstValue.address)
        }
    }

    @Test
    fun getCountriesListShouldCallCountriesLoaded() {
        given(countriesUseCase.buildUseCaseSingle(any())).willReturn(Single.just(countryList))
        presenter.getCountries()

        val inOrder = inOrder(view, countriesUseCase)
        inOrder.verify(countriesUseCase).execute(any(), any(), any())
        inOrder.verify(view).countriesLoaded(countryList)
    }

    @Test
    fun getCountriesListCallAddressChangedWhenOnSingleContentError() {
        val error = Error.Content()
        given(countriesUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.getCountries()

        argumentCaptor<Error>().apply {
            val inOrder = inOrder(view, countriesUseCase)
            inOrder.verify(countriesUseCase).execute(any(), any(), any())
            inOrder.verify(view).showError(capture())

            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun getCountriesListCallAddressChangedWhenOnSingleNonCriticalError() {
        val message = "message"
        val error = Error.NonCritical(message)
        given(countriesUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.getCountries()

        val inOrder = inOrder(view, countriesUseCase)
        inOrder.verify(countriesUseCase).execute(any(), any(), any())
        inOrder.verify(view).showMessage(message)
    }
}