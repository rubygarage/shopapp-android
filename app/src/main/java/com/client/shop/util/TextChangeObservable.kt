package com.client.shop.util

import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable
import io.reactivex.disposables.Disposables

class TextChangeObservable constructor(val view: TextView,
                                       private val withInitValue: Boolean) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>?) {
        observer?.let {
            if (!checkMainThread(it)) {
                return
            }
            val listener = Listener(view, it, withInitValue)
            it.onSubscribe(listener)
            view.addTextChangedListener(listener)
        }
    }

    internal class Listener(private val view: TextView,
                            private val observer: Observer<in String>,
                            withInitValue: Boolean) : MainThreadDisposable(), TextWatcher {
        init {
            if (withInitValue && !isDisposed) {
                observer.onNext("")
            }
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            if (!isDisposed) {
                observer.onNext(s.toString())
            }
        }

        override fun afterTextChanged(s: Editable) {}

        override fun onDispose() {
            view.removeTextChangedListener(this)
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