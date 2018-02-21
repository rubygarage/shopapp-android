package com.shopapp.data.impl

import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
import com.shopapp.gateway.entity.Error
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.AuthRepository
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

    @Before
    fun setUpTest() {
        MockitoAnnotations.initMocks(this)
        repository = AuthRepositoryImpl(api)
    }

    @Test
    fun changePasswordShouldDelegateCallToApi() {
        repository.changePassword("").subscribe()
        verify(api).changePassword(eq(""), any())
    }

    @Test
    fun changePasswordShouldCompleteOnApiResult() {
        val observer = TestObserver<Unit>()
        given(api.changePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })
        repository.changePassword("").subscribe(observer)
        observer.assertComplete()
    }

    @Test
    fun changePasswordShouldErrorOnApiFailure() {
        val observer = TestObserver<Unit>()
        val error = Error.Content()
        given(api.changePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.changePassword("").subscribe(observer)
        observer.assertError(error)
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
    	val observer = TestObserver<Unit>()
        given(api.signIn(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onResult(Unit)
        })
        repository.signIn("", "").subscribe(observer)
        observer.assertComplete()
    }

    @Test
    fun signInShouldErrorOnApiFailure() {
        val error = Error.Content()
        val observer = TestObserver<Unit>()
        given(api.signIn(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onFailure(error)
        })

        repository.signIn("", "").subscribe(observer)
        observer.assertError(error)
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
        val observer = TestObserver<Unit>()

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
        ).subscribe(observer)

        observer.assertComplete()
    }

    @Test
    fun signUpShouldErrorOnApiFailure() {
        val observer = TestObserver<Unit>()
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
        ).subscribe(observer)

        observer.assertError(error)
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
        val observer = TestObserver<Customer>()
        given(api.editCustomerInfo(any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(3)
            callback.onResult(Unit)
        })
        repository.editCustomer("name", "lastName", "123654789").subscribe(observer)
        observer.assertComplete()
    }

    @Test
    fun editCustomerShouldErrorOnApiFailure() {
        val error = Error.Content()
        val observer = TestObserver<Customer>()
        given(api.editCustomerInfo(any(), any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(3)
            callback.onFailure(error)
        })
        repository.editCustomer("name", "lastName", "123654789").subscribe(observer)
        observer.assertError(error)
    }
}