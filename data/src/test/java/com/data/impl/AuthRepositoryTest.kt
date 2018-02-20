package com.data.impl

import com.client.shop.gateway.Api
import com.client.shop.gateway.ApiCallback
import com.client.shop.gateway.entity.Error
import com.data.RxImmediateSchedulerRule
import com.domain.repository.AuthRepository
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@Suppress("FunctionName")
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
        val observer: TestObserver<Unit> = TestObserver()
        given(api.changePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })
        repository.changePassword("").subscribe(observer)
        observer.assertComplete()
    }

    @Test
    fun changePasswordShouldErrorOnApiFailure() {
        val observer: TestObserver<Unit> = TestObserver()
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
        val observer: TestObserver<Unit> = TestObserver()
        given(api.signIn(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onResult(Unit)
        })
        repository.signIn("", "").subscribe(observer)
        observer.assertComplete()
    }

    @Test
    fun signInShouldErrorOnApiFailure() {
        val observer: TestObserver<Unit> = TestObserver()
        val error = Error.Content()
        given(api.signIn(any(), any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(2)
            callback.onFailure(error)
        })

        repository.signIn("", "").subscribe(observer)
        observer.assertError(error)
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
}