package com.shopapp.data.rx

import com.nhaarman.mockito_kotlin.*
import com.shopapp.gateway.entity.Error
import io.reactivex.CompletableEmitter
import io.reactivex.SingleEmitter
import org.junit.Before
import org.junit.Test

class RxCallbackSingleTest {

    private lateinit var callback: RxCallbackSingle<String>
    private lateinit var emitter: SingleEmitter<String>

    @Before
    fun setUp() {
        emitter = mock()
        callback = RxCallbackSingle(emitter)
    }

    @Test
    fun shouldCompleteOnResult() {
        val result = "test"
        given(emitter.isDisposed).willReturn(false)
        callback.onResult(result)
        verify(emitter).onSuccess(result)
    }

    @Test
    fun shouldNotCompleteOnResult() {
        val result = "test"
        given(emitter.isDisposed).willReturn(true)
        callback.onResult(result)
        verify(emitter, never()).onSuccess(any())
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