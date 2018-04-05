package com.shopapp.ui.splash.contract

import com.shopapp.domain.interactor.cart.CartValidationUseCase
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface SplashView : BaseLceView<Unit> {

    fun validationError()
}

class SplashPresenter @Inject constructor(private val cartValidationUseCase: CartValidationUseCase) :
    BaseLcePresenter<Unit, SplashView>(cartValidationUseCase) {

    fun validateItems() {
        cartValidationUseCase.execute(
            { view?.showContent(Unit) },
            {
                it.printStackTrace()
                view?.validationError()
            },
            Unit
        )
    }
}