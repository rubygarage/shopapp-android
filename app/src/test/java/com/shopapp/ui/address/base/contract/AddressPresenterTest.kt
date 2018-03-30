package com.shopapp.ui.address.base.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.R
import com.shopapp.domain.interactor.account.CreateCustomerAddressUseCase
import com.shopapp.domain.interactor.account.EditCustomerAddressUseCase
import com.shopapp.domain.interactor.account.GetCountriesUseCase
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
    private lateinit var createCustomerAddressUseCase: CreateCustomerAddressUseCase

    @Mock
    private lateinit var editCustomerAddressUseCase: EditCustomerAddressUseCase

    @Mock
    private lateinit var address: Address

    @Mock
    private lateinit var countryList: List<Country>

    private lateinit var presenter: AddressPresenter<AddressView>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = AddressPresenter(fieldValidator, countriesUseCase, createCustomerAddressUseCase,
            editCustomerAddressUseCase)
        presenter.attachView(view)

        countriesUseCase.mock()
        createCustomerAddressUseCase.mock()
        editCustomerAddressUseCase.mock()
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
        given(createCustomerAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.just(result))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitAddress(address)

        val inOrder = inOrder(view, createCustomerAddressUseCase)
        inOrder.verify(createCustomerAddressUseCase).execute(any(), any(), eq(address))
        inOrder.verify(view).addressChanged(address)
    }

    @Test
    fun submitAddressShouldCallAddressChangedWhenOnSingleContentError() {
        val error = Error.Content()
        given(createCustomerAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitAddress(address)

        argumentCaptor<Error>().apply {
            val inOrder = inOrder(view, createCustomerAddressUseCase)
            inOrder.verify(createCustomerAddressUseCase).execute(any(), any(), eq(address))
            inOrder.verify(view).showError(capture())
            inOrder.verify(view).addressChanged(address)

            assertTrue(firstValue is Error.Content)
        }
    }

    @Test
    fun submitAddressShouldCallAddressChangedWhenOnSingleNonCriticalError() {
        val message = "message"
        val error = Error.NonCritical(message)
        given(createCustomerAddressUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.submitAddress(address)

        val inOrder = inOrder(view, createCustomerAddressUseCase)
        inOrder.verify(createCustomerAddressUseCase).execute(any(), any(), eq(address))
        inOrder.verify(view).showMessage(message)
        inOrder.verify(view).addressChanged(address)
    }

    @Test
    fun editAddressShouldShowErrorWhenEditInvalidAddress() {
        given(fieldValidator.isAddressValid(any())).willReturn(false)
        presenter.editAddress(MockInstantiator.DEFAULT_ID, address)

        val inOrder = inOrder(view)
        inOrder.verify(view).submitAddressError()
        inOrder.verify(view).showMessage(R.string.invalid_address)
    }

    @Test
    fun editAddressShouldCallAddressChangedWhenOnCompletableSuccess() {
        given(editCustomerAddressUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.editAddress(MockInstantiator.DEFAULT_ID, address)

        argumentCaptor<EditCustomerAddressUseCase.Params>().apply {
            val inOrder = inOrder(view, editCustomerAddressUseCase)
            inOrder.verify(editCustomerAddressUseCase).execute(any(), any(), capture())
            inOrder.verify(view).addressChanged(address)

            assertEquals(MockInstantiator.DEFAULT_ID, firstValue.addressId)
            assertEquals(address, firstValue.address)
        }
    }

    @Test
    fun editAddressShouldCallAddressChangedWhenOnCompletableContentError() {
        val error = Error.Content()
        given(editCustomerAddressUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.editAddress(MockInstantiator.DEFAULT_ID, address)


        val inOrder = inOrder(view, editCustomerAddressUseCase)
        argumentCaptor<EditCustomerAddressUseCase.Params>().apply {
            inOrder.verify(editCustomerAddressUseCase).execute(any(), any(), capture())
            assertEquals(MockInstantiator.DEFAULT_ID, firstValue.addressId)
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
        given(editCustomerAddressUseCase.buildUseCaseCompletable(any())).willReturn(Completable.error(error))
        given(fieldValidator.isAddressValid(any())).willReturn(true)
        presenter.editAddress(MockInstantiator.DEFAULT_ID, address)

        argumentCaptor<EditCustomerAddressUseCase.Params>().apply {
            val inOrder = inOrder(view, editCustomerAddressUseCase)
            inOrder.verify(editCustomerAddressUseCase).execute(any(), any(), capture())
            inOrder.verify(view).showMessage(message)
            inOrder.verify(view).addressChanged(address)

            assertEquals(MockInstantiator.DEFAULT_ID, firstValue.addressId)
            assertEquals(address, firstValue.address)
        }
    }

    @Test
    fun getCountriesListShouldCallCountriesLoaded() {
        given(countriesUseCase.buildUseCaseSingle(any())).willReturn(Single.just(countryList))
        presenter.getCountriesList()

        val inOrder = inOrder(view, countriesUseCase)
        inOrder.verify(countriesUseCase).execute(any(), any(), any())
        inOrder.verify(view).countriesLoaded(countryList)
    }

    @Test
    fun getCountriesListCallAddressChangedWhenOnSingleContentError() {
        val error = Error.Content()
        given(countriesUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.getCountriesList()

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
        presenter.getCountriesList()

        val inOrder = inOrder(view, countriesUseCase)
        inOrder.verify(countriesUseCase).execute(any(), any(), any())
        inOrder.verify(view).showMessage(message)
    }
}