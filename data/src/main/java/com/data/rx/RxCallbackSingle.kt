package com.data.rx

import com.domain.entity.Error
import com.domain.network.ApiCallback
import io.reactivex.SingleEmitter

class RxCallbackSingle<in T>(private val emitter: SingleEmitter<T>) : ApiCallback<T> {

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