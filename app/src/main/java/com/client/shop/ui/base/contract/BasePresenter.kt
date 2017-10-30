package com.client.shop.ui.base.contract

import com.domain.entity.Error
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.repository.Repository
import io.reactivex.disposables.CompositeDisposable

open class BasePresenter<in M, V : BaseMvpView<M>>(protected val repository: Repository) : MvpBasePresenter<V>() {

    protected var disposables = CompositeDisposable()

    open fun doAction(action: String) {

    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        disposables.clear()
    }

    protected fun resolveError(error: Throwable): Boolean {
        error.printStackTrace()
        return when (error) {
            is Error.Content -> {
                view?.showError(error.isNetworkError)
                true
            }
            is Error.Critical -> {
                //TODO ADD ROUTING
                true
            }
            else -> false
        }
    }
}