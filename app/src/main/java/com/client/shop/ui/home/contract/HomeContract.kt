package com.client.shop.ui.home.contract

import android.util.Log
import com.client.shop.const.Constant.MAXIMUM_PER_PAGE_COUNT
import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Category
import com.domain.entity.ProductVariant
import com.domain.entity.Shop
import com.repository.Repository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface HomeView : BaseMvpView<Pair<Shop, List<Category>>>

class HomePresenter @Inject constructor(repository: Repository) : BasePresenter<Pair<Shop, List<Category>>, HomeView>(repository) {

    fun requestData() {

        repository.getCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            Log.d("TEST", result.toString())
                        },
                        { error ->
                            error.printStackTrace()
                            Log.d("TEST", error.message)
                        },
                        {
                            Log.d("TEST", "COMPLETE")
                        })

        repository.addProductToCart(ProductVariant("2", "Test", "2", true, listOf()), 2)

        val disposable = Single.zip<Shop, List<Category>, Pair<Shop, List<Category>>>(repository.getShop(),
                repository.getCategoryList(MAXIMUM_PER_PAGE_COUNT), BiFunction { t1, t2 -> Pair(t1, t2) })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result -> view?.showContent(result) },
                        { e -> resolveError(e) })

        disposables.add(disposable)
    }
}