package com.client.shop.ui.recent.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.rx.RxCallback
import com.shopapicore.ShopApiCore
import com.shopapicore.entity.Product
import com.shopapicore.entity.SortType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface RecentView : BaseMvpView {

    fun productListLoaded(productList: List<Product>)
}

class RecentPresenter @Inject constructor(private val shopApiCore: ShopApiCore) : BasePresenter<RecentView>() {

    fun loadProductList(perPage: Int, paginationValue: String? = null) {

        showProgress()

        val call = Observable.create<List<Product>> { emitter ->
            shopApiCore.getProductList(perPage, paginationValue, SortType.RECENT, true,
                    RxCallback<List<Product>>(emitter))
        }

        val productDisposable = call.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.productListLoaded(result)
                    }
                }, { error ->
                    error.printStackTrace()
                    hideProgress()
                }, {
                    hideProgress()
                })
        disposables.add(productDisposable)
    }
}