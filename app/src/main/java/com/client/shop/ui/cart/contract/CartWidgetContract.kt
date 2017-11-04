package com.client.shop.ui.cart.contract

import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.hannesdorfmann.mosby3.mvp.MvpView
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface CartWidgetView : MvpView {

    fun changeBadgeCount(count: Int)
}

class CartWidgetPresenter @Inject constructor(private val repository: Repository) :
        MvpBasePresenter<CartWidgetView>() {

    private var disposable: Disposable? = null

    override fun attachView(view: CartWidgetView?) {
        super.attachView(view)
        disposable = repository.getCartProductList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> view?.changeBadgeCount(result.size) },
                        { error -> error.printStackTrace() })
    }

    override fun detachView(retainInstance: Boolean) {
        super.detachView(retainInstance)
        disposable?.let {
            if (!it.isDisposed) {
                it.dispose()
            }
        }
    }
}