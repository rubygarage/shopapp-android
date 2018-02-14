package com.domain.interactors

import com.domain.RxImmediateSchedulerRule
import com.client.shop.getaway.entity.Error
import com.domain.interactor.base.SingleUseCase
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.annotation.Config

@Suppress("FunctionName")
@RunWith(MockitoJUnitRunner::class)
@Config(manifest = Config.NONE)
class SingleUseCaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private val returnedValue = Any()

    private lateinit var useCase: SingleUseCase<Any, Any>

    private var onComplete: ((t: Any) -> Unit) = mock {
        onGeneric { invoke(any()) } doReturn Unit
    }

    private var onError: ((t: Throwable) -> Unit) = mock {
        onGeneric { invoke(any()) } doReturn Unit
    }

    @Test
    fun singleUseCaseTest_Execute_HappyCase() {
        useCase = object : SingleUseCase<Any, Any>() {
            override fun buildUseCaseSingle(params: Any): Single<Any> {
                return Single.just(returnedValue)
            }
        }

        with(useCase) {
            attachToLifecycle()
            execute(onComplete, onError, "")
        }

        verify(onComplete).invoke(returnedValue)
        verify(onError, never()).invoke(any())

        Assert.assertTrue(useCase.isAttachedToLifecycle())
        Assert.assertEquals(1, useCase.disposables.size())

        useCase.dispose()

        Assert.assertEquals(0, useCase.disposables.size())
    }

    @Test
    fun singleUseCaseTest_Execute_SadCase() {
        val error = Error.Critical()
        useCase = object : SingleUseCase<Any, Any>() {
            override fun buildUseCaseSingle(params: Any): Single<Any> {
                return Single.error(error)
            }
        }

        with(useCase) {
            attachToLifecycle()
            execute(onComplete, onError, "")
        }

        verify(onComplete, never()).invoke(any())
        verify(onError).invoke(error)

        Assert.assertTrue(useCase.isAttachedToLifecycle())
        Assert.assertEquals(1, useCase.disposables.size())

        useCase.dispose()

        Assert.assertEquals(0, useCase.disposables.size())
    }
}