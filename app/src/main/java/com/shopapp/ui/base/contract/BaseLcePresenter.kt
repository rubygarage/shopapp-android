package com.shopapp.ui.base.contract

import com.shopapp.domain.interactor.base.UseCase
import com.shopapp.gateway.entity.Error

open class BaseLcePresenter<in M, V : BaseLceView<M>>(vararg useCases: UseCase) : BasePresenter<V>(*useCases) {

    open fun doAction(action: String) {

    }

    protected open fun resolveError(error: Throwable): Boolean {
        error.printStackTrace()
        return when (error) {
            is Error.Content,
            is Error.Critical -> {
                (error as? Error)?.let { view?.showError(it) }
                true
            }
            is Error.NonCritical -> {
                view?.showMessage(error.message)
                true
            }
            else -> false
        }
    }
}