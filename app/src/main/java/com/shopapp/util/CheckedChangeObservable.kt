package com.shopapp.util

import android.widget.CompoundButton
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class CheckedChangeObservable(private val view: CompoundButton) : Observable<Boolean>() {

    override fun subscribeActual(observer: Observer<in Boolean>?) {
        observer?.let {
            if (!RxUtil.checkMainThread(it)) {
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

}