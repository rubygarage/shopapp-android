package com.client.shop.ui.base.contract

import io.reactivex.disposables.Disposable


abstract class UseCase {

    protected var disposable: Disposable? = null

    fun dispose() {
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}