package com.domain.interactor.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class UseCase {

    private var isAttachedToLifecycle = false
    protected var lastDisposable: Disposable? = null
    protected var disposables: CompositeDisposable = CompositeDisposable()

    protected fun checkIsAttachedToLifecycle() {
        if (!isAttachedToLifecycle) {
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
        isAttachedToLifecycle = true
    }

    fun dispose() {
        disposables.clear()
    }
}