package com.client.shop.ui.recent.contract

import com.client.shop.ui.base.contract.BaseMvpViewLce
import com.client.shop.ui.base.contract.BasePresenterLce
import com.domain.entity.Product
import com.domain.entity.SortType
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface RecentView : BaseMvpViewLce<List<Product>>

class RecentPresenter @Inject constructor(repository: Repository) : BasePresenterLce<List<Product>, RecentView>(repository) {

    fun loadProductList(perPage: Int, paginationValue: String? = null) {

        //TODO MOVE REVERSE TO SHOPIFY
        val productDisposable = repository.getProductList(perPage, paginationValue, SortType.RECENT, true)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> view?.showContent(result) },
                        { error -> error.printStackTrace() })
        disposables.add(productDisposable)
    }
}