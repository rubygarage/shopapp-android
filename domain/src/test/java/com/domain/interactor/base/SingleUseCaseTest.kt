package com.domain.interactor.base

import com.client.shop.gateway.entity.Error
import com.domain.RxImmediateSchedulerRule
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
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
        Assert.assertEquals(1, useCase.disposables.size())
    }

    @Test
    fun shouldClearDisposablesOnDispose() {
        val result: Any = mock()
        useCase = object : SingleUseCase<Any, Unit>() {
            override fun buildUseCaseSingle(params: Unit) = Single.just(result)
        }
        useCase.attachToLifecycle()

        useCase.execute(onSuccess, onError, Unit)
        Assert.assertEquals(1, useCase.disposables.size())

        useCase.dispose()
        Assert.assertEquals(0, useCase.disposables.size())
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
}