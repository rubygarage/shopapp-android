package com.domain.interactor.base

import com.client.shop.getaway.entity.Error
import com.domain.RxImmediateSchedulerRule
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@Suppress("FunctionName")
@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
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
        Assert.assertEquals(1, useCase.disposables.size())
    }

    @Test
    fun shouldClearDisposablesOnDispose() {
        useCase = object : CompletableUseCase<Any>() {
            override fun buildUseCaseCompletable(params: Any) = Completable.complete()
        }
        useCase.attachToLifecycle()

        useCase.execute(onComplete, onError, "")
        Assert.assertEquals(1, useCase.disposables.size())

        useCase.dispose()
        Assert.assertEquals(0, useCase.disposables.size())
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
}