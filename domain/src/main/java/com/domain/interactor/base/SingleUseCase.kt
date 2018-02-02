package com.domain.interactor.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<T, in Params> : UseCase() {

    private var lastDisposable: Disposable? = null

    abstract fun buildUseCaseSingle(params: Params): Single<T>

    fun execute(onSuccess: ((t: T) -> Unit), onError: ((t: Throwable) -> Unit), params: Params) {
        checkIsAttachedToLifecycle()
        val disposable = buildUseCaseSingle(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        disposables.add(disposable)
        lastDisposable = disposable
    }

    fun executeWithDisposeLatest(onSuccess: ((t: T) -> Unit), onError: ((t: Throwable) -> Unit), params: Params) {
        lastDisposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
        execute(onSuccess, onError, params)
    }
}