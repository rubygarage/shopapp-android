package com.client.shop.ui.cart.contract

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import javax.inject.Inject

interface CartWidgetView : MvpView {

    fun changeBadgeCount(count: Int)
}

class CartWidgetPresenter @Inject constructor(private val cartItemsUseCase: CartItemsUseCase) :
        MvpBasePresenter<CartWidgetView>() {

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