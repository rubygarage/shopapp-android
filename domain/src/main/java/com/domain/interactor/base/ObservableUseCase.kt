package com.domain.interactor.base

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class ObservableUseCase<T, in Params> : UseCase() {

    abstract fun buildUseCaseObservable(params: Params): Observable<T>

    fun execute(onSuccess: ((t: T) -> Unit), onError: ((t: Throwable) -> Unit),
                onComplete: (() -> Unit), params: Params) {
        checkIsAttachedToLifecycle()
        disposable = buildUseCaseObservable(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess, onError, onComplete)
    }
}