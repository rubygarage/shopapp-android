package com.shopapp.ui.base.contract

import com.shopapp.domain.interactor.base.UseCase
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView

open class BasePresenter<V : MvpView>(vararg useCases: UseCase) : MvpBasePresenter<V>() {

    private var useCaseList: MutableList<UseCase> = mutableListOf()

    init {
        useCaseList.addAll(
            useCases.map {
                it.attachToLifecycle()
                it
            }
        )
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        for (useCase in useCaseList) {
            useCase.dispose()
        }
    }
}