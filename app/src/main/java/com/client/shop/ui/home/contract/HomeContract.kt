package com.client.shop.ui.home.contract

import com.client.shop.const.Constant.MAXIMUM_PER_PAGE_COUNT
import com.client.shop.ui.base.contract.BaseMvpViewLce
import com.client.shop.ui.base.contract.BasePresenterLce
import com.domain.entity.Category
import com.domain.entity.Shop
import com.repository.Repository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

interface HomeView : BaseMvpViewLce<Pair<Shop, List<Category>>>

class HomePresenter @Inject constructor(repository: Repository) : BasePresenterLce<Pair<Shop, List<Category>>, HomeView>(repository) {

    fun requestData() {

        val disposable = Single.zip<Shop, List<Category>, Pair<Shop, List<Category>>>(repository.getShop(),
                repository.getCategoryList(MAXIMUM_PER_PAGE_COUNT), BiFunction { t1, t2 -> Pair(t1, t2) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> view?.showContent(result) },
                        { e -> resolveError(e) })

        disposables.add(disposable)
    }
}