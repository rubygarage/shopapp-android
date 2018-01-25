package com.client.shop.util

import android.os.Looper
import android.widget.CompoundButton
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.Disposables

class CompoundButtonCheckedChangeObservable(private val view: CompoundButton) : Observable<Boolean>() {

    override fun subscribeActual(observer: Observer<in Boolean>?) {
        observer?.let {
            if (!checkMainThread(it)) {
                return
            }
            val listener = Listener(view, it)
            it.onSubscribe(listener)
            view.setOnCheckedChangeListener(listener)
        }
    }

    internal class Listener(
        private val view: CompoundButton,
        private val observer: Observer<in Boolean>) : MainThreadDisposable(), CompoundButton.OnCheckedChangeListener {

        override fun onCheckedChanged(compoundButton: CompoundButton, isChecked: Boolean) {
            if (!isDisposed) {
                observer.onNext(isChecked)
            }
        }

        override fun onDispose() {
            view.setOnCheckedChangeListener(null)
        }
    }

    private fun checkMainThread(observer: Observer<*>): Boolean {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            observer.onSubscribe(Disposables.empty())
            observer.onError(IllegalStateException(
                "Expected to be called on the main thread but was " + Thread.currentThread().name))
            return false
        }
        return true
    }
}