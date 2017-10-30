package com.repository.rx

import com.apicore.ApiCallback
import com.domain.entity.Error
import io.reactivex.SingleEmitter

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