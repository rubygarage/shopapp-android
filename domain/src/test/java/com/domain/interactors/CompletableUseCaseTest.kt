package com.domain.interactors

import com.domain.RxImmediateSchedulerRule
import com.domain.entity.Error
import com.domain.interactor.base.CompletableUseCase
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

    @Test
    fun completableUseCaseTest_Execute_HappyCase() {
        useCase = object : CompletableUseCase<Any>() {
            override fun buildUseCaseCompletable(params: Any) = Completable.complete()
        }
        useCase.attachToLifecycle()
        useCase.execute(onComplete, onError, "")
        verify(onComplete).invoke()
        verify(onError, never()).invoke(any())
        Assert.assertTrue(useCase.isAttachedToLifecycle())
        Assert.assertEquals(1, useCase.disposables.size())
        useCase.dispose()
        Assert.assertEquals(0, useCase.disposables.size())
    }

    @Test
    fun completableUseCaseTest_Execute_SadCase() {
        val error = Error.Critical()
        useCase = object : CompletableUseCase<Any>() {
            override fun buildUseCaseCompletable(params: Any) = Completable.error(error)
        }
        useCase.attachToLifecycle()
        useCase.execute(onComplete, onError, "")
        verify(onComplete, never()).invoke()
        verify(onError).invoke(error)
        Assert.assertTrue(useCase.isAttachedToLifecycle())
        Assert.assertEquals(1, useCase.disposables.size())
        useCase.dispose()
        Assert.assertEquals(0, useCase.disposables.size())
    }
}