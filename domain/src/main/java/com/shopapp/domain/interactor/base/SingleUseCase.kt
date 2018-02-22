package com.shopapp.domain.interactor.base

import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<T, in Params> : UseCase() {

    abstract fun buildUseCaseSingle(params: Params): Single<T>

    fun execute(onSuccess: ((t: T) -> Unit), onError: ((t: Throwable) -> Unit), params: Params) {
        checkIsAttachedToLifecycle()
        disposeLatest()
        val disposable = buildUseCaseSingle(params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(onSuccess, onError)
        disposables.add(disposable)
        lastDisposable = disposable
    }
}