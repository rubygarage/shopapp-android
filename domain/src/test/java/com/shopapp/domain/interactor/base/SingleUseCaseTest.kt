package com.shopapp.domain.interactor.base

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.RxImmediateSchedulerRule
import com.shopapp.gateway.entity.Error
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class SingleUseCaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var useCase: SingleUseCase<Any, Unit>

    private var onSuccess: ((t: Any) -> Unit) = mock {
        onGeneric { invoke(any()) } doReturn Unit
    }

    private var onError: ((t: Throwable) -> Unit) = mock {
        onGeneric { invoke(any()) } doReturn Unit
    }

    @Test(expected = RuntimeException::class)
    fun shouldErrorOnIgnoreAttachingToLifecycle() {
        val result: Any = mock()
        useCase = object : SingleUseCase<Any, Unit>() {
            override fun buildUseCaseSingle(params: Unit) = Single.just(result)
        }
        useCase.execute(onSuccess, onError, Unit)
    }

    @Test
    fun shouldSaveDisposablesAfterExecute() {
        val result: Any = mock()
        useCase = object : SingleUseCase<Any, Unit>() {
            override fun buildUseCaseSingle(params: Unit) = Single.just(result)
        }
        useCase.attachToLifecycle()

        useCase.execute(onSuccess, onError, Unit)
        assertEquals(1, useCase.disposables.size())
    }

    @Test
    fun shouldClearDisposablesOnDispose() {
        val result: Any = mock()
        useCase = object : SingleUseCase<Any, Unit>() {
            override fun buildUseCaseSingle(params: Unit) = Single.just(result)
        }
        useCase.attachToLifecycle()

        useCase.execute(onSuccess, onError, Unit)
        assertEquals(1, useCase.disposables.size())

        useCase.dispose()
        assertEquals(0, useCase.disposables.size())
    }

    @Test
    fun shouldDisposeLastDisposable() {
        val result: Any = mock()
        val useCase = object : TestSingleUseCase<Any, Unit>() {
            override fun buildUseCaseSingle(params: Unit) = Single.create<Any> {
                Single.just(run {
                    Thread.sleep(100)
                    result
                })
            }
        }
        useCase.attachToLifecycle()

        useCase.execute(onSuccess, onError, Unit)
        val firstDisposable = useCase.publicLastDisposable()
        assertFalse(firstDisposable!!.isDisposed)

        useCase.execute(onSuccess, onError, Unit)
        val secondDisposable = useCase.publicLastDisposable()
        assertNotSame(firstDisposable, secondDisposable)
        assertTrue(firstDisposable.isDisposed)
        assertFalse(secondDisposable!!.isDisposed)

        secondDisposable.dispose()
    }

    @Test
    fun shouldInvokeOnCompleteOnSuccess() {
        val result: Any = mock()
        useCase = object : SingleUseCase<Any, Unit>() {
            override fun buildUseCaseSingle(params: Unit) = Single.just(result)
        }
        useCase.attachToLifecycle()
        useCase.execute(onSuccess, onError, Unit)

        verify(onSuccess).invoke(any())
        verify(onError, never()).invoke(any())
    }

    @Test
    fun shouldInvokeOnErrorOnFailure() {
        val error = Error.Critical()
        useCase = object : SingleUseCase<Any, Unit>() {
            override fun buildUseCaseSingle(params: Unit): Single<Any> = Single.error(error)
        }
        useCase.attachToLifecycle()
        useCase.execute(onSuccess, onError, Unit)
        verify(onSuccess, never()).invoke(any())
        verify(onError).invoke(error)
    }

    private abstract class TestSingleUseCase<T, in Params> : SingleUseCase<T, Params>() {

        fun publicLastDisposable() = lastDisposable
    }
}