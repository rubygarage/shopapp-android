package com.data.repository

import com.client.shop.getaway.Api
import com.client.shop.getaway.ApiCallback
import com.client.shop.getaway.entity.Error
import com.data.RxImmediateSchedulerRule
import com.data.impl.AuthRepositoryImpl
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
import org.robolectric.annotation.Config

@Suppress("FunctionName")
@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
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
    fun authRepositoryTest_ChangePassword_HappyCase() {

        given(api.changePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onResult(Unit)
        })

        val observer: TestObserver<Unit> = TestObserver()
        repository.changePassword("").subscribe(observer)
        verify(api).changePassword(eq(""), any())
        observer.assertComplete()
    }

    @Test
    fun authRepositoryTest_ChangePassword_SadCase() {
        val error = Error.Content()
        given(api.changePassword(any(), any())).willAnswer({
            val callback = it.getArgument<ApiCallback<Unit>>(1)
            callback.onFailure(error)
        })

        val observer: TestObserver<Unit> = TestObserver()
        repository.changePassword("").subscribe(observer)
        verify(api).changePassword(eq(""), any())
        observer.assertError(error)
    }

}