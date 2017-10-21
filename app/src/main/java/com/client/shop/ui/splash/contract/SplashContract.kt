package com.client.shop.ui.splash.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Category
import com.domain.entity.Shop
import com.repository.Repository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

interface SplashView : BaseMvpView {

    fun dataReceived(shop: Shop, categories: List<Category>)
}

class SplashPresenter @Inject constructor(repository: Repository) : BasePresenter<SplashView>(repository) {

    fun requestShop() {

        showProgress()

        val disposable = Single.zip<Shop, List<Category>, Pair<Shop, List<Category>>>(repository.getShop(),
                repository.getCategoryList(10), BiFunction { t1, t2 -> Pair(t1, t2) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (shop, categories) ->
                    if (isViewAttached) {
                        view.dataReceived(shop, categories)
                        view.hideProgress()
                    }
                }, { error ->
                    showMessage(error.message!!, true)
                    hideProgress()
                })

        disposables.add(disposable)
    }
}