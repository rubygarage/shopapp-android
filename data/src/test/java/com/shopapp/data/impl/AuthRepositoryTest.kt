package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.*
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.AuthRepository
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

class AuthRepositoryTest {

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

    private lateinit var repository: AuthRepository
    private lateinit var customerObserver: TestObserver<Customer>
    private lateinit var unitObserver: TestObserver<Unit>
    private lateinit var booleanObserver: TestObserver<Boolean>
    private lateinit var stringObserver: TestObserver<String>
    private lateinit var countriesObserver: TestObserver<List<Country>>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = AuthRepositoryImpl(api)
        customerObserver = TestObserver()
        unitObserver = TestObserver()
        booleanObserver = TestObserver()
        stringObserver = TestObserver()
        countriesObserver = TestObserver()
    }

    @Test
    fun changePasswordShouldDelegateCallToApi() {
        repository.changePassword("").subscribe()
        verify(api).changePassword(eq(""), any())
    }

    @Test
    fun changePasswordShouldCompleteOnApiResult() {
        given(api.changePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })
        repository.changePassword("").subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun changePasswordShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.changePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.changePassword("").subscribe(unitObserver)
        unitObserver.assertError(error)
    }

    @Test
    fun signInShouldDelegateCallToApi() {
        val email = "test@test.com"
        val password = "123456789"
        repository.signIn(email, password).subscribe()
        verify(api).signIn(eq(email), eq(password), any())
    }

    @Test
    fun signInShouldCompleteOnApiResult() {
        given(api.signIn(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onResult(Unit)
        })
        repository.signIn("", "").subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun signInShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.signIn(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onFailure(error)
        })

        repository.signIn("", "").subscribe(unitObserver)
        unitObserver.assertError(error)
    }

    @Test
    fun signUpShouldDelegateCallToApi() {
        repository.signUp(
            "test",
            "test",
            "test@email.com",
            "123456789",
            "+38063329670"
        ).subscribe()

        verify(api).signUp(
            eq("test"),
            eq("test"),
            eq("test@email.com"),
            eq("123456789"),
            eq("+38063329670"),
            any()
        )
    }

    @Test
    fun signUpShouldCompleteOnApiResult() {

        given(api.signUp(any(), any(), any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(5)
            callback.onResult(Unit)
        })

        repository.signUp(
            "test",
            "test",
            "test@email.com",
            "123456789",
            "+38063329670"
        ).subscribe(unitObserver)

        unitObserver.assertComplete()
    }

    @Test
    fun signUpShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.signUp(any(), any(), any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(5)
            callback.onFailure(error)
        })

        repository.signUp(
            "test",
            "test",
            "test@email.com",
            "123456789",
            "+38063329670"
        )
            .subscribe(unitObserver)

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
    fun editCustomerShouldDelegateCallToApi() {
        val name = "name"
        val lastName = "lastName"
        val phone = "0633291677"
        repository.editCustomer(name, lastName, phone).subscribe()
        verify(api).editCustomerInfo(eq(name), eq(lastName), eq(phone), any())
    }

    @Test
    fun editCustomerShouldCompleteOnApiResult() {
        given(api.editCustomerInfo(any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(3)
            callback.onResult(Unit)
        })
        repository.editCustomer("name", "lastName", "123654789").subscribe(customerObserver)
        customerObserver.assertComplete()
    }

    @Test
    fun editCustomerShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.editCustomerInfo(any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(3)
            callback.onFailure(error)
        })
        repository.editCustomer("name", "lastName", "123654789").subscribe(customerObserver)
        customerObserver.assertError(error)
    }

    @Test
    fun forgotPasswordShouldDelegateCallToApi() {
        val email = "test@test.com"
        repository.forgotPassword(email).subscribe()
        verify(api).forgotPassword(eq(email), any())
    }

    @Test
    fun forgotPasswordShouldCompleteOnApiResult() {
        val email = "test@test.com"
        given(api.forgotPassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })

        repository.forgotPassword(email).subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun forgotPasswordShouldErrorOnApiFailure() {
        val observer: TestObserver<Unit> = TestObserver()
        val email = "test@test.com"
        val error = Error.Content()
        given(api.forgotPassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.forgotPassword(email).subscribe(observer)
        observer.assertError(error)
    }

    @Test
    fun updateAccountSettingsShouldDelegateCallToApi() {
        repository.updateAccountSettings(true).subscribe()
        verify(api).updateCustomerSettings(eq(true), any())
    }

    @Test
    fun updateAccountSettingsShouldCompleteOnApiResult() {
        given(api.updateCustomerSettings(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })

        repository.updateAccountSettings(true).subscribe(booleanObserver)
        booleanObserver.assertComplete()
    }

    @Test
    fun updateAccountSettingsShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.updateCustomerSettings(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.updateAccountSettings(true).subscribe(booleanObserver)
        booleanObserver.assertError(error)
    }

    @Test
    fun isLoggedCheckShouldDelegateCallToApi() {
        repository.isLoggedIn().subscribe()
        verify(api).isLoggedIn(any())
    }

    @Test
    fun isLoggedCheckCompleteOnApiResult() {
        given(api.isLoggedIn(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Boolean>>(0)
            callback.onResult(false)
        })
        repository.isLoggedIn().subscribe(booleanObserver)
        booleanObserver.assertComplete()
        booleanObserver.assertValue(false)
    }

    @Test
    fun isLoggedCheckShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.isLoggedIn(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Boolean>>(0)
            callback.onFailure(error)
        })
        repository.isLoggedIn().subscribe(booleanObserver)
        booleanObserver.assertError(error)
    }

    @Test
    fun signOutShouldDelegateCallToApi() {
        repository.signOut().subscribe()
        verify(api).signOut(any())
    }

    @Test
    fun signOutCheckCompleteOnApiResult() {
        given(api.signOut(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(0)
            callback.onResult(Unit)
        })
        repository.signOut().subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun signOutCheckShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.signOut(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(0)
            callback.onFailure(error)
        })
        repository.signOut().subscribe(unitObserver)
        unitObserver.assertError(error)
    }

    @Test
    fun createCustomerAddressShouldDelegateCallToApi() {
        repository.createCustomerAddress(address).subscribe()
        verify(api).createCustomerAddress(eq(address), any())
    }

    @Test
    fun createCustomerAddressShouldCompleteOnApiResult() {
        val addressId = "addressId"
        given(api.createCustomerAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<String>>(1)
            callback.onResult(addressId)
        })
        repository.createCustomerAddress(address).subscribe(stringObserver)
        stringObserver.assertValue(addressId)
    }

    @Test
    fun createCustomerAddressShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.createCustomerAddress(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })
        repository.createCustomerAddress(address).subscribe(stringObserver)
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
    fun editCustomerAddressShouldDelegateCallToApi() {
        val address: Address = mock()
        repository.editCustomerAddress(ADDRESS_ID, address).subscribe()
        verify(api).editCustomerAddress(eq(ADDRESS_ID), eq(address), any())
    }

    @Test
    fun editCustomerAddressShouldCompleteOnApiResult() {
        val address: Address = mock()
        given(api.editCustomerAddress(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onResult(Unit)
        })
        repository.editCustomerAddress(ADDRESS_ID, address).subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun editCustomerAddressShouldErrorOnApiFailure() {
        val address: Address = mock()
        val error = Error.Content()
        given(api.editCustomerAddress(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onFailure(error)
        })
        repository.editCustomerAddress(ADDRESS_ID, address).subscribe(unitObserver)
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