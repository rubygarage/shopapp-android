package com.client.shop.ui.recent.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Product
import com.domain.entity.SortType
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface RecentView : BaseMvpView {

    fun productListLoaded(productList: List<Product>)
}

class RecentPresenter @Inject constructor(repository: Repository) : BasePresenter<RecentView>(repository) {

    fun loadProductList(perPage: Int, paginationValue: String? = null) {

        showProgress()

        //TODO MOVE RECENT TO SHOPIFY
        val productDisposable = repository.getProductList(perPage, paginationValue, SortType.RECENT, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.productListLoaded(result)
                        view.hideProgress()
                    }
                }, { error ->
                    error.printStackTrace()
                    hideProgress()
                })
        disposables.add(productDisposable)
    }
}