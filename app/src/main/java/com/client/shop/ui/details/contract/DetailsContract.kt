package com.client.shop.ui.details.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Product
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface DetailsView : BaseMvpView {

    fun productLoaded(product: Product)
}

class DetailsPresenter @Inject constructor(repository: Repository) : BasePresenter<DetailsView>(repository) {

    fun loadProductDetails(productId: String) {

        showProgress()

        disposables.add(repository.getProduct(productId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.productLoaded(result)
                        view.hideProgress()
                    }
                }, { _ -> hideProgress() }))
    }
}