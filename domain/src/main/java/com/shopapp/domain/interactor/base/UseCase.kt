package com.shopapp.domain.interactor.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class UseCase {

    private var isAttached = false
    protected var lastDisposable: Disposable? = null
    var disposables: CompositeDisposable = CompositeDisposable()

    fun checkIsAttachedToLifecycle() {
        if (!isAttachedToLifecycle()) {
            throw RuntimeException("You have to attach ${this.javaClass.name} to the lifecycle.")
        }
    }

    protected fun disposeLatest() {
        lastDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }

    fun attachToLifecycle() {
        isAttached = true
    }

    fun dispose() {
        disposables.clear()
    }

    fun isAttachedToLifecycle() = isAttached
}