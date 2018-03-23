package com.shopapp.ui.cart.contract

import com.hannesdorfmann.mosby3.mvp.MvpView
import com.shopapp.domain.interactor.cart.CartItemsUseCase
import com.shopapp.ui.base.contract.BasePresenter
import javax.inject.Inject

interface CartWidgetView : MvpView {

    fun changeBadgeCount(count: Int)
}

class CartWidgetPresenter @Inject constructor(private val cartItemsUseCase: CartItemsUseCase) :
    BasePresenter<CartWidgetView>(cartItemsUseCase) {

    override fun attachView(cartWidgetView: CartWidgetView?) {
        super.attachView(cartWidgetView)

        cartItemsUseCase.execute(
            { view?.changeBadgeCount(it.size) },
            { it.printStackTrace() },
            { },
            Unit
        )
    }
}