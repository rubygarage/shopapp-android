package com.domain.interactor.base

import io.reactivex.disposables.CompositeDisposable

abstract class UseCase {

    private var isAttachedToLifecycle = false
    protected var disposables: CompositeDisposable = CompositeDisposable()

    protected fun checkIsAttachedToLifecycle() {
        if (!isAttachedToLifecycle) {
            throw RuntimeException("You have to attach ${this.javaClass.name} to the lifecycle.")
        }
    }

    fun attachToLifecycle() {
        isAttachedToLifecycle = true
    }

    fun dispose() {
        disposables.clear()
    }
}