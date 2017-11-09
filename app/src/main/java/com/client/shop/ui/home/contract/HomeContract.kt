package com.client.shop.ui.home.contract

import com.client.shop.const.Constant.MAXIMUM_PER_PAGE_COUNT
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Category
import com.domain.entity.Shop
import com.repository.Repository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function3
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface HomeView : BaseMvpView<Triple<Shop, List<Category>, Boolean>>

class HomePresenter @Inject constructor(repository: Repository) :
        BasePresenter<Triple<Shop, List<Category>, Boolean>, HomeView>(repository) {

    fun requestData() {

        val disposable = Single.zip<Shop, List<Category>, Boolean, Triple<Shop, List<Category>, Boolean>>(
                repository.getShop(),
                repository.getCategoryList(MAXIMUM_PER_PAGE_COUNT),
                repository.isLoggedIn(),
                Function3<Shop, List<Category>, Boolean, Triple<Shop, List<Category>, Boolean>>
                { shop, categories, isAuthorized ->
                    Triple(shop, categories, isAuthorized)
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { view?.showContent(it) },
                        { resolveError(it) })

        disposables.add(disposable)
    }
}