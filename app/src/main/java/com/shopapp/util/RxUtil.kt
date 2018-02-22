package com.shopapp.util

import android.os.Looper
import io.reactivex.Observer
import io.reactivex.disposables.Disposables

object RxUtil {

    fun checkMainThread(observer: Observer<*>): Boolean {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onSubscribe(Disposables.empty())
            observer.onError(IllegalStateException(
                "Expected to be called on the main thread but was " + Thread.currentThread().name))
            return false
        }
        return true
    }

}