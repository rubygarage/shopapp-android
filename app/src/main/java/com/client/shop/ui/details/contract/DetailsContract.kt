package com.client.shop.ui.details.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Product
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface DetailsView : BaseMvpView<Product>

class DetailsPresenter @Inject constructor(repository: Repository) : BasePresenter<Product, DetailsView>(repository) {

    fun loadProductDetails(productId: String) {

        val detailsDisposable = repository.getProduct(productId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> view?.showContent(result) },
                        { e -> resolveError(e) }
                )

        disposables.add(detailsDisposable)
    }
}