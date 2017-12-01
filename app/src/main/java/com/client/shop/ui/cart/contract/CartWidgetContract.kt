package com.client.shop.ui.cart.contract

import com.domain.interactor.cart.CartItemsUseCase
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.ui.base.contract.BasePresenter
import javax.inject.Inject

interface CartWidgetView : MvpView {

    fun changeBadgeCount(count: Int)
}

class CartWidgetPresenter @Inject constructor(private val cartItemsUseCase: CartItemsUseCase) :
        BasePresenter<CartWidgetView>(cartItemsUseCase) {

    override fun attachView(view: CartWidgetView?) {
        super.attachView(view)

        cartItemsUseCase.execute(
                { view?.changeBadgeCount(it.size) },
                { it.printStackTrace() },
                { },
                Unit
        )
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        cartItemsUseCase.dispose()
    }
}