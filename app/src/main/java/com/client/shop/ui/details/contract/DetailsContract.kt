package com.client.shop.ui.details.contract

import com.client.shop.ui.base.contract.BaseMvpViewLce
import com.client.shop.ui.base.contract.BasePresenterLce
import com.domain.entity.Product
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface DetailsView : BaseMvpViewLce<Product>

class DetailsPresenter @Inject constructor(repository: Repository) : BasePresenterLce<Product, DetailsView>(repository) {

    fun loadProductDetails(productId: String) {

        disposables.add(repository.getProduct(productId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> view?.showContent(result) },
                        { e -> resolveError(e) }
                ))
    }
}