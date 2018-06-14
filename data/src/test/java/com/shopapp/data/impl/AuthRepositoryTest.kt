package com.shopapp.data.impl

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.eq
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import com.shopapp.data.RxImmediateSchedulerRule
import com.shopapp.domain.repository.AuthRepository
import com.shopapp.gateway.Api
import com.shopapp.gateway.ApiCallback
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

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Mock
    private lateinit var api: Api

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
    fun resetPasswordShouldDelegateCallToApi() {
        val email = "test@test.com"
        repository.resetPassword(email).subscribe()
        verify(api).resetPassword(eq(email), any())
    }

    @Test
    fun resetPasswordShouldCompleteOnApiResult() {
        val email = "test@test.com"
        given(api.resetPassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })

        repository.resetPassword(email).subscribe(unitObserver)
        unitObserver.assertComplete()
    }

    @Test
    fun resetPasswordShouldErrorOnApiFailure() {
        val observer: TestObserver<Unit> = TestObserver()
        val email = "test@test.com"
        val error = Error.Content()
        given(api.resetPassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        repository.resetPassword(email).subscribe(observer)
        observer.assertError(error)
    }

    @Test
    fun isSignedInCheckShouldDelegateCallToApi() {
        repository.isSignedIn().subscribe()
        verify(api).isSignedIn(any())
    }

    @Test
    fun isSignedInCheckCompleteOnApiResult() {
        given(api.isSignedIn(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Boolean>>(0)
            callback.onResult(false)
        })
        repository.isSignedIn().subscribe(booleanObserver)
        booleanObserver.assertComplete()
        booleanObserver.assertValue(false)
    }

    @Test
    fun isSignedInCheckShouldErrorOnApiFailure() {
        val error = Error.Content()
        given(api.isSignedIn(any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Boolean>>(0)
            callback.onFailure(error)
        })
        repository.isSignedIn().subscribe(booleanObserver)
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
}