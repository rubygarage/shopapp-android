package com.domain.interactor.base

import io.reactivex.disposables.Disposable

abstract class UseCase {

    private var isAttachedToLifecycle = false

    protected var disposable: Disposable? = null

    protected fun checkIsAttachedToLifecycle() {
        if (!isAttachedToLifecycle) {
            throw RuntimeException("You must attach ${this.javaClass.name} to the lifecycle.")
        }
    }

    fun attachToLifecycle() {
        isAttachedToLifecycle = true
    }

    fun dispose() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}