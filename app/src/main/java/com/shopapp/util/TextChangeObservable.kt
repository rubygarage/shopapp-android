package com.shopapp.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class TextChangeObservable constructor(val view: TextView,
                                       private val withInitValue: Boolean = false) : Observable<String>() {

    override fun subscribeActual(observer: Observer<in String>?) {
        observer?.let {
            if (!RxUtil.checkMainThread(it)) {
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

}