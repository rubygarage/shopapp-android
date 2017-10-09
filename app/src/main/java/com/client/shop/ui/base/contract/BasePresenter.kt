package com.client.shop.ui.base.contract

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import io.reactivex.disposables.CompositeDisposable


open class BasePresenter<V : BaseMvpView> : MvpBasePresenter<V>() {

    protected var disposables = CompositeDisposable()

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)

        disposables.clear()
    }

    protected fun showProgress() {
        if (isViewAttached) {
            view.showProgress()
        }
    }

    protected fun hideProgress() {
        if (isViewAttached) {
            view.hideProgress()
        }
    }

    protected fun showMessage(message: String, isError: Boolean) {
        if (isViewAttached) {
            view.showMessage(message, isError)
        }
    }

}