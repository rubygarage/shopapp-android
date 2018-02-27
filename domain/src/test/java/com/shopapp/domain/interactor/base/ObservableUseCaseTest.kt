package com.shopapp.domain.interactor.base

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.RxImmediateSchedulerRule
import com.shopapp.gateway.entity.Error
import io.reactivex.Observable
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class ObservableUseCaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var useCase: ObservableUseCase<Any, Unit>

    private var onSuccess: ((t: Any) -> Unit) = mock {
        onGeneric { invoke(any()) } doReturn Unit
    }

    private var onError: ((t: Throwable) -> Unit) = mock {
        onGeneric { invoke(any()) } doReturn Unit
    }

    private var onComplete: (() -> Unit) = mock {
        onGeneric { invoke() } doReturn Unit
    }

    @Test(expected = RuntimeException::class)
    fun shouldErrorOnIgnoreAttachingToLifecycle() {
        val result: Any = mock()
        useCase = object : ObservableUseCase<Any, Unit>() {
            override fun buildUseCaseObservable(params: Unit) = Observable.just(result)
        }
        useCase.execute(onSuccess, onError, onComplete, Unit)
    }

    @Test
    fun shouldSaveDisposablesAfterExecute() {
        val result: Any = mock()
        useCase = object : ObservableUseCase<Any, Unit>() {
            override fun buildUseCaseObservable(params: Unit) = Observable.just(result)
        }
        useCase.attachToLifecycle()

        useCase.execute(onSuccess, onError, onComplete, Unit)
        assertEquals(1, useCase.disposables.size())
    }

    @Test
    fun shouldClearDisposablesOnDispose() {
        val result: Any = mock()
        useCase = object : ObservableUseCase<Any, Unit>() {
            override fun buildUseCaseObservable(params: Unit) = Observable.just(result)
        }
        useCase.attachToLifecycle()

        useCase.execute(onSuccess, onError, onComplete, Unit)
        assertEquals(1, useCase.disposables.size())

        useCase.dispose()
        assertEquals(0, useCase.disposables.size())
    }

    @Test
    fun shouldInvokeOnCompleteOnSuccess() {
        val result: Any = mock()
        useCase = object : ObservableUseCase<Any, Unit>() {
            override fun buildUseCaseObservable(params: Unit) = Observable.just(result)
        }
        useCase.attachToLifecycle()

        useCase.execute(onSuccess, onError, onComplete, Unit)
        verify(onSuccess).invoke(any())
        verify(onComplete).invoke()
        verify(onError, never()).invoke(any())
    }

    @Test
    fun shouldInvokeOnErrorOnFailure() {
        val error = Error.Critical()
        useCase = object : ObservableUseCase<Any, Unit>() {
            override fun buildUseCaseObservable(params: Unit): Observable<Any> =
                Observable.error(error)
        }
        useCase.attachToLifecycle()

        useCase.execute(onSuccess, onError, onComplete, Unit)
        verify(onError).invoke(error)
        verify(onSuccess, never()).invoke(any())
        verify(onComplete, never()).invoke()
    }
}