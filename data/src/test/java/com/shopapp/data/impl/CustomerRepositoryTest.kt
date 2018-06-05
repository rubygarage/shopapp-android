package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.*
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.CustomerRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Address
import com.shopapp.gateway.entity.Country
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.Error
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class CustomerRepositoryTest {

    companion object {
        private const val ADDRESS_ID = "address_id"
    }

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    @Mock
    private lateinit var address: Address

    private lateinit var repository: CustomerRepository
    private lateinit var customerObserver: TestObserver<Customer>
    private lateinit var unitObserver: TestObserver<Unit>
    private lateinit var booleanObserver: TestObserver<Boolean>
    private lateinit var stringObserver: TestObserver<String>
    private lateinit var countriesObserver: TestObserver<List<Country>>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = CustomerRepositoryImpl(api)
        customerObserver = TestObserver()
        unitObserver = TestObserver()
        booleanObserver = TestObserver()
        stringObserver = TestObserver()
        countriesObserver = TestObserver()
    }

    @Test
    fun updatePasswordShouldDelegateCallToApi() {
        repository.updatePassword("").subscribe()
        verify(api).updatePassword(eq(""), any())
    }

    @Test
    fun updatePasswordShouldCompleteOnApiResult() {
        given(api.updatePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })
        repository.updatePassword("").subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun updatePasswordShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.updatePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.updatePassword("").subscribe(unitObserver)
        unitObserver.assertError(error)
    }

    @Test
    fun getCustomerShouldCompleteOnApiResult() {
        val observer = TestObserver<Customer>()
        given(api.getCustomer(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(0)
            callback.onResult(Unit)
        })
        repository.getCustomer().subscribe(observer)
        observer.assertComplete()
    }

    @Test
    fun getCustomerShouldErrorOnApiFailure() {
        val error = Error.Content()
        val observer = TestObserver<Customer>()
        given(api.getCustomer(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(0)
            callback.onFailure(error)
        })
        repository.getCustomer().subscribe(observer)
        observer.assertError(error)
    }

    @Test
    fun updateCustomerShouldDelegateCallToApi() {
        val name = "name"
        val lastName = "lastName"
        val phone = "0633291677"
        repository.updateCustomer(name, lastName, phone).subscribe()
        verify(api).updateCustomer(eq(name), eq(lastName), eq(phone), any())
    }

    @Test
    fun updateCustomerShouldCompleteOnApiResult() {
        given(api.updateCustomer(any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(3)
            callback.onResult(Unit)
        })
        repository.updateCustomer("name", "lastName", "123654789").subscribe(customerObserver)
        customerObserver.assertComplete()
    }

    @Test
    fun updateCustomerShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.updateCustomer(any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(3)
            callback.onFailure(error)
        })
        repository.updateCustomer("name", "lastName", "123654789").subscribe(customerObserver)
        customerObserver.assertError(error)
    }

    @Test
    fun updateAccountSettingsShouldDelegateCallToApi() {
        repository.updateCustomerSettings(true).subscribe()
        verify(api).updateCustomerSettings(eq(true), any())
    }

    @Test
    fun updateAccountSettingsShouldCompleteOnApiResult() {
        given(api.updateCustomerSettings(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })

        repository.updateCustomerSettings(true).subscribe(booleanObserver)
        booleanObserver.assertComplete()
    }

    @Test
    fun updateAccountSettingsShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.updateCustomerSettings(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.updateCustomerSettings(true).subscribe(booleanObserver)
        booleanObserver.assertError(error)
    }

    @Test
    fun addCustomerAddressShouldDelegateCallToApi() {
        repository.addCustomerAddress(address).subscribe()
        verify(api).addCustomerAddress(eq(address), any())
    }

    @Test
    fun addCustomerAddressShouldCompleteOnApiResult() {
        given(api.addCustomerAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })
        repository.addCustomerAddress(address).subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun addCustomerAddressShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.addCustomerAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })
        repository.addCustomerAddress(address).subscribe(stringObserver)
        stringObserver.assertError(error)
    }

    @Test
    fun getCountriesShouldDelegateCallToApi() {
        repository.getCountries().subscribe()
        verify(api).getCountries(any())
    }

    @Test
    fun getCountriesShouldCompleteOnApiResult() {
        val countries = listOf<Country>()
        given(api.getCountries(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Country>>>(0)
            callback.onResult(countries)
        })
        repository.getCountries().subscribe(countriesObserver)
        countriesObserver.assertValue(countries)
    }

    @Test
    fun getCountriesShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.getCountries(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<List<Country>>>(0)
            callback.onFailure(error)
        })
        repository.getCountries().subscribe(countriesObserver)
        countriesObserver.assertError(error)
    }

    @Test
    fun updateCustomerAddressShouldDelegateCallToApi() {
        val address: Address = mock()
        repository.updateCustomerAddress(address).subscribe()
        verify(api).updateCustomerAddress(eq(address), any())
    }

    @Test
    fun updateCustomerAddressShouldCompleteOnApiResult() {
        val address: Address = mock()
        given(api.updateCustomerAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })
        repository.updateCustomerAddress(address).subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun updateCustomerAddressShouldErrorOnApiFailure() {
        val address: Address = mock()
        val error = Error.Content()
        given(api.updateCustomerAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })
        repository.updateCustomerAddress(address).subscribe(unitObserver)
        unitObserver.assertError(error)
    }

    @Test
    fun deleteCustomerAddressShouldDelegateCallToApi() {
        repository.deleteCustomerAddress(ADDRESS_ID).subscribe()
        verify(api).deleteCustomerAddress(eq(ADDRESS_ID), any())
    }

    @Test
    fun deleteCustomerAddressShouldCompleteOnApiResult() {
        given(api.deleteCustomerAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })
        repository.deleteCustomerAddress(ADDRESS_ID).subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun deleteCustomerAddressShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.deleteCustomerAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })
        repository.deleteCustomerAddress(ADDRESS_ID).subscribe(unitObserver)
        unitObserver.assertError(error)
    }

    @Test
    fun setDefaultShippingAddressShouldDelegateCallToApi() {
        repository.setDefaultShippingAddress(ADDRESS_ID).subscribe()
        verify(api).setDefaultShippingAddress(eq(ADDRESS_ID), any())
    }

    @Test
    fun setDefaultShippingAddressShouldCompleteOnApiResult() {
        given(api.setDefaultShippingAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })
        repository.setDefaultShippingAddress(ADDRESS_ID).subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun setDefaultShippingAddressShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.setDefaultShippingAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })
        repository.setDefaultShippingAddress(ADDRESS_ID).subscribe(unitObserver)
        unitObserver.assertError(error)
    }
}