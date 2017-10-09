package com.client.shop.ui.category.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.client.shop.ui.base.rx.RxCallback
import com.shopapicore.ShopApiCore
import com.shopapicore.entity.Category
import com.shopapicore.entity.Product
import com.shopapicore.entity.SortType
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface CategoryView : BaseMvpView {

    fun productListLoaded(productList: List<Product>?)
}

class CategoryPresenter @Inject constructor(private val shopApiCore: ShopApiCore) : BasePresenter<CategoryView>() {

    fun loadProductList(perPage: Int, paginationValue: String?, categoryId: String, sortType: SortType) {

        showProgress()

        val reverse = sortType == SortType.RECENT

        val call = Observable.create<Category> { emitter ->
            shopApiCore.getCategoryDetails(categoryId, perPage, paginationValue, sortType, reverse, RxCallback<Category>(emitter))
        }

        disposables.add(call
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.productListLoaded(result.categoryDetails.productList)
                    }
                }, { error ->
                    error.printStackTrace()
                    hideProgress()
                }, {
                    hideProgress()
                }))
    }
}