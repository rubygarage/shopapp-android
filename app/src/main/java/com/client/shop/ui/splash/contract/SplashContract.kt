package com.client.shop.ui.splash.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.rx.RxCallback
import com.shopapicore.ShopApiCore
import com.shopapicore.entity.Category
import com.shopapicore.entity.Shop
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

interface SplashView : BaseMvpView {

    fun dataReceived(shop: Shop, categories: List<Category>)
}

class SplashPresenter @Inject constructor(private val shopApiCore: ShopApiCore) : BasePresenter<SplashView>() {

    fun requestShop() {

        showProgress()

        val shopCall = Observable.create<Shop> { emitter ->
            shopApiCore.getShopInfo(RxCallback<Shop>(emitter))
        }

        val categoriesCall = Observable.create<List<Category>> { emitter ->
            shopApiCore.getCategoryList(10, null, null, false, RxCallback<List<Category>>(emitter))
        }

        val disposable = Observable.zip<Shop, List<Category>, Pair<Shop, List<Category>>>(shopCall,
                categoriesCall, BiFunction { t1, t2 -> Pair(t1, t2) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ (shop, categories) ->
                    if (isViewAttached) {
                        view.dataReceived(shop, categories)
                    }
                }, { error ->
                    showMessage(error.message!!, true)
                    hideProgress()
                }, { hideProgress() })
        disposables.add(disposable)
    }
}