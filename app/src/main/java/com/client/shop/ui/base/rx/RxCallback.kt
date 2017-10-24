package com.client.shop.ui.base.rx

import com.apicore.ApiCallback
import io.reactivex.SingleEmitter
import java.lang.Error

class RxCallback<in T>(private val emitter: SingleEmitter<T>) : ApiCallback<T> {

    override fun onResult(result: T) {
        if (!emitter.isDisposed) {
            emitter.onSuccess(result)
        }
    }

    override fun onFailure(error: Error) {
        if (!emitter.isDisposed) {
            emitter.onError(error)
        }
    }
}