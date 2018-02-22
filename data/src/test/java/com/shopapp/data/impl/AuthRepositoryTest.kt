package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Customer
import com.shopapp.gateway.entity.Error
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

    private lateinit var repository: AuthRepository

    private lateinit var observerCustomer: TestObserver<Customer>

    private lateinit var observerUnit: TestObserver<Unit>

    private lateinit var observerBoolean: TestObserver<Boolean>

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = AuthRepositoryImpl(api)
        observerCustomer = TestObserver()
        observerUnit = TestObserver()
        observerBoolean = TestObserver()
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
        repository.changePassword("").subscribe(observerUnit)
        observerUnit.assertComplete()
    }

    @Test
    fun changePasswordShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.changePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.changePassword("").subscribe(observerUnit)
        observerUnit.assertError(error)
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
        repository.signIn("", "").subscribe(observerUnit)
        observerUnit.assertComplete()
    }

    @Test
    fun signInShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.signIn(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onFailure(error)
        })

        repository.signIn("", "").subscribe(observerUnit)
        observerUnit.assertError(error)
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
        ).subscribe(observerUnit)

        observerUnit.assertComplete()
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
        ).subscribe(observerUnit)

        observerUnit.assertError(error)
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
        repository.editCustomer("name", "lastName", "123654789").subscribe(observerCustomer)
        observerCustomer.assertComplete()
    }

    @Test
    fun editCustomerShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.editCustomerInfo(any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(3)
            callback.onFailure(error)
        })
        repository.editCustomer("name", "lastName", "123654789").subscribe(observerCustomer)
        observerCustomer.assertError(error)
    }

    @Test
    fun forgotPasswordShouldDelegateCallToApi() {
        val email = "test@test.com"
        repository.forgotPassword(email).subscribe()
        verify(api).forgotPassword(eq(email), any())
    }

    @Test
    fun forgotPasswordShouldCompleteOnApiResult() {
        val observer: TestObserver<Unit> = TestObserver()
        val email = "test@test.com"
        given(api.forgotPassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })

        repository.forgotPassword(email).subscribe(observer)
        observer.assertComplete()
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

        repository.updateAccountSettings(true).subscribe(observerBoolean)
        observerBoolean.assertComplete()
    }

    @Test
    fun updateAccountSettingsShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.forgotPassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.updateAccountSettings(true).subscribe(observerBoolean)
        observerBoolean.assertError(error)
    }
}