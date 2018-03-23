package com.shopapp.domain.interactor.base

import com.nhaarman.mockito_kotlin.*
import com.shopapp.domain.RxImmediateSchedulerRule
import com.shopapp.gateway.entity.Error
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class CompletableUseCaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var useCase: CompletableUseCase<Any>

    private var onComplete: (() -> Unit) = mock {
        onGeneric { invoke() } doReturn Unit
    }

    private var onError: ((t: Throwable) -> Unit) = mock {
        onGeneric { invoke(any()) } doReturn Unit
    }

    @Test(expected = RuntimeException::class)
    fun shouldErrorOnIgnoreAttachingToLifecycle() {
        useCase = object : CompletableUseCase<Any>() {
            override fun buildUseCaseCompletable(params: Any) = Completable.complete()
        }
        useCase.execute(onComplete, onError, "")
    }

    @Test
    fun shouldSaveDisposablesAfterExecute() {
        useCase = object : CompletableUseCase<Any>() {
            override fun buildUseCaseCompletable(params: Any) = Completable.complete()
        }
        useCase.attachToLifecycle()

        useCase.execute(onComplete, onError, "")
        assertEquals(1, useCase.disposables.size())
    }

    @Test
    fun shouldClearDisposablesOnDispose() {
        useCase = object : CompletableUseCase<Any>() {
            override fun buildUseCaseCompletable(params: Any) = Completable.complete()
        }
        useCase.attachToLifecycle()

        useCase.execute(onComplete, onError, "")
        assertEquals(1, useCase.disposables.size())

        useCase.dispose()
        assertEquals(0, useCase.disposables.size())
    }

    @Test
    fun shouldDisposeLastDisposable() {
        val useCase = object : TestCompletableUseCase<Unit>() {
            override fun buildUseCaseCompletable(params: Unit) = Completable.create {
                Single.just(run {
                    Thread.sleep(100)
                })
            }
        }
        useCase.attachToLifecycle()

        useCase.execute(onComplete, onError, Unit)
        val firstDisposable = useCase.publicLastDisposable()
        assertFalse(firstDisposable!!.isDisposed)

        useCase.execute(onComplete, onError, Unit)
        val secondDisposable = useCase.publicLastDisposable()
        assertNotSame(firstDisposable, secondDisposable)
        assertTrue(firstDisposable.isDisposed)
        assertFalse(secondDisposable!!.isDisposed)

        secondDisposable.dispose()
    }

    @Test
    fun shouldInvokeOnCompleteOnSuccess() {
        useCase = object : CompletableUseCase<Any>() {
            override fun buildUseCaseCompletable(params: Any) = Completable.complete()
        }
        useCase.attachToLifecycle()
        useCase.execute(onComplete, onError, "")

        verify(onComplete).invoke()
        verify(onError, never()).invoke(any())
    }

    @Test
    fun shouldInvokeOnErrorOnFailure() {
        val error = Error.Critical()
        useCase = object : CompletableUseCase<Any>() {
            override fun buildUseCaseCompletable(params: Any) = Completable.error(error)
        }
        useCase.attachToLifecycle()
        useCase.execute(onComplete, onError, "")
        verify(onComplete, never()).invoke()
        verify(onError).invoke(error)
    }

    private abstract class TestCompletableUseCase<in Params> : CompletableUseCase<Params>() {

        fun publicLastDisposable() = lastDisposable
    }
}