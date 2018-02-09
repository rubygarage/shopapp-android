package com.data.rx

import com.client.shop.getaway.ApiCallback
import com.client.shop.getaway.entity.Error
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