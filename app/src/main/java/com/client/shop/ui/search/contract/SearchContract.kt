package com.client.shop.ui.search.contract

import android.text.TextUtils
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.rx.RxCallback
import com.shopapicore.ShopApiCore
import com.shopapicore.entity.Product
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface SearchView : BaseMvpView {

    fun searchResultsReceived(productList: List<Product>, query: String)
}

class SearchPresenter @Inject constructor(private val shopApiCore: ShopApiCore) : BasePresenter<SearchView>() {

    fun search(perPage: Int, paginationValue: String?, query: String) {

        if (TextUtils.isEmpty(query)) {
            view.searchResultsReceived(listOf(), query)
            return
        }

        showProgress()

        val call = Observable.create<List<Product>> { emitter ->
            shopApiCore.searchProductList(perPage, paginationValue, query, RxCallback<List<Product>>(emitter))
        }

        val searchDisposable = call.observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.searchResultsReceived(result, query)
                    }
                }, { error ->
                    error.printStackTrace()
                    hideProgress()
                }, { hideProgress() })
        disposables.add(searchDisposable)
    }
}