package com.ui.base.contract

import com.domain.entity.Error
import com.domain.interactor.base.UseCase
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter

open class BasePresenter<in M, V : BaseView<M>>(useCases: Array<UseCase>) : MvpBasePresenter<V>() {

    private var useCaseList: MutableList<UseCase> = mutableListOf()

    init {
        useCaseList.addAll(useCases)
    }

    open fun doAction(action: String) {

    }

    open protected fun resolveError(error: Throwable): Boolean {
        error.printStackTrace()
        return when (error) {
            is Error.Content -> {
                view?.showError(error.isNetworkError)
                true
            }
            is Error.NonCritical -> {
                view?.showMessage(error.message)
                true
            }
            is Error.Critical -> {
                //TODO ADD ROUTING
                true
            }
            else -> false
        }
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        for (useCase in useCaseList) {
            useCase.dispose()
        }
    }
}