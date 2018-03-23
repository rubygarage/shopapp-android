package com.shopapp.data.rx

import com.nhaarman.mockito_kotlin.*
import com.shopapp.gateway.entity.Error
import io.reactivex.CompletableEmitter
import org.junit.Before
import org.junit.Test

class RxCallbackCompletableTest {

    private lateinit var callback: RxCallbackCompletable
    private lateinit var emitter: CompletableEmitter

    @Before
    fun setUp() {
        emitter = mock()
        callback = RxCallbackCompletable(emitter)
    }

    @Test
    fun shouldCompleteOnResult() {
        given(emitter.isDisposed).willReturn(false)
        callback.onResult(Unit)
        verify(emitter).onComplete()
    }

    @Test
    fun shouldNotCompleteOnResult() {
        given(emitter.isDisposed).willReturn(true)
        callback.onResult(Unit)
        verify(emitter, never()).onComplete()
    }


    @Test
    fun shouldErrorOnFailure() {
        val error = Error.Content()
        given(emitter.isDisposed).willReturn(false)
        callback.onFailure(error)
        verify(emitter).onError(error)
    }

    @Test
    fun shouldNotErrorOnFailure() {
        given(emitter.isDisposed).willReturn(true)
        callback.onFailure(Error.Content())
        verify(emitter, never()).onError(any())
    }
}