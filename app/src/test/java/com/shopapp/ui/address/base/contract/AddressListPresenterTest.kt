package com.shopapp.ui.address.base.contract

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.interactor.account.DeleteCustomerAddressUseCase
import com.shopapp.domain.interactor.account.GetCustomerUseCase
import com.shopapp.domain.interactor.account.SetDefaultAddressUseCase
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.Error
import com.shopapp.test.MockInstantiator
import com.shopapp.test.RxImmediateSchedulerRule
import com.shopapp.test.ext.mock
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class AddressListPresenterTest {

    companion object {
        private const val DEFAULT_ADDRESS_ID = "default_address_id"
        private const val SIMPLE_ADDRESS_ID = "simple_address_id"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var view: AddressListView

    @Mock
    private lateinit var getCustomerUseCase: GetCustomerUseCase

    @Mock
    private lateinit var deleteCustomerAddressUseCase: DeleteCustomerAddressUseCase

    @Mock
    private lateinit var setDefaultAddressUseCase: SetDefaultAddressUseCase

    private lateinit var customer: Customer
    private lateinit var defaultAddress: Address
    private lateinit var simpleAddress: Address

    private lateinit var presenter: AddressListPresenter<AddressListView>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        presenter = AddressListPresenter(getCustomerUseCase, deleteCustomerAddressUseCase, setDefaultAddressUseCase)
        presenter.attachView(view)

        getCustomerUseCase.mock()
        deleteCustomerAddressUseCase.mock()
        setDefaultAddressUseCase.mock()

        customer = MockInstantiator.newCustomer()
        defaultAddress = MockInstantiator.newAddress()
        given(defaultAddress.id).willReturn(DEFAULT_ADDRESS_ID)
        simpleAddress = MockInstantiator.newAddress()
        given(simpleAddress.id).willReturn(SIMPLE_ADDRESS_ID)
        given(customer.defaultAddress).willReturn(defaultAddress)
        given(customer.addressList).willReturn(listOf(simpleAddress, defaultAddress))
    }

    @Test
    fun getAddressListShouldShowContentWhenOnSingleSuccess() {
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.just(customer))

        assertEquals(simpleAddress, customer.addressList.first())
        presenter.getAddressList()

        argumentCaptor<Pair<Address, List<Address>>>().apply {
            val inOrder = inOrder(view, getCustomerUseCase)
            inOrder.verify(getCustomerUseCase).execute(any(), any(), any())
            inOrder.verify(view).showContent(capture())

            assertEquals(customer.defaultAddress, firstValue.first)
            assertEquals(defaultAddress, firstValue.second[0])
            assertEquals(simpleAddress, firstValue.second[1])
        }
    }

    @Test
    fun getAddressListShouldShowContentWhenOnSingleError() {
        val error = Error.Content()
        given(getCustomerUseCase.buildUseCaseSingle(any())).willReturn(Single.error(error))
        presenter.getAddressList()

        argumentCaptor<Pair<Address, List<Address>>>().apply {
            val inOrder = inOrder(view, getCustomerUseCase)
            inOrder.verify(getCustomerUseCase).execute(any(), any(), any())
            inOrder.verify(view).showContent(capture())

            assertNull(firstValue.first)
            assertTrue(firstValue.second.isEmpty())
        }
    }

    @Test
    fun shouldDeleteAddressWhenOnCompletableSuccess() {
        given(deleteCustomerAddressUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())
        presenter.deleteAddress(MockInstantiator.DEFAULT_ID)

        verify(deleteCustomerAddressUseCase).execute(any(), any(), eq(MockInstantiator.DEFAULT_ID))
    }

    @Test
    fun setDefaultAddressShouldShowContentWhenOnCompletableSuccess() {
        given(setDefaultAddressUseCase.buildUseCaseCompletable(any())).willReturn(Completable.complete())

        assertEquals(simpleAddress, customer.addressList.first())
        presenter.setDefaultAddress(defaultAddress, customer.addressList)

        argumentCaptor<Pair<Address, List<Address>>>().apply {
            val inOrder = inOrder(view, setDefaultAddressUseCase)
            inOrder.verify(setDefaultAddressUseCase).execute(any(), any(), eq(DEFAULT_ADDRESS_ID))
            inOrder.verify(view).showContent(capture())

            assertEquals(customer.defaultAddress, firstValue.first)
            assertEquals(defaultAddress, firstValue.second[0])
            assertEquals(simpleAddress, firstValue.second[1])
        }
    }
}