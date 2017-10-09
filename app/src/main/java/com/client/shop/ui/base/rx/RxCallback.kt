package com.client.shop.ui.base.rx

import com.shopapicore.ApiCallback
import io.reactivex.ObservableEmitter
import java.lang.Error

class RxCallback<T>(private val emitter: ObservableEmitter<T>) : ApiCallback<T> {

    override fun onResult(result: T) {
        if (!emitter.isDisposed) {
            emitter.onNext(result)
            emitter.onComplete()
        }
    }

    override fun onFailure(error: Error) {
        if (!emitter.isDisposed) {
            emitter.onError(error)
        }
    }
}