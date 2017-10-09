package com.client.shop.ui.details.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.rx.RxCallback
import com.shopapicore.ShopApiCore
import com.shopapicore.entity.Product
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface DetailsView : BaseMvpView {

    fun productLoaded(product: Product)
}

class DetailsPresenter @Inject constructor(private val shopApiCore: ShopApiCore) : BasePresenter<DetailsView>() {

    fun loadProductDetails(productId: String) {

        showProgress()

        val call = Observable.create<Product> { emitter ->
            shopApiCore.getProduct(productId, RxCallback<Product>(emitter))
        }

        disposables.add(call
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.productLoaded(result)
                    }
                }, { _ -> hideProgress() }, { hideProgress() }))
    }
}