package com.client.shop.ui.category.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Product
import com.domain.entity.SortType
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface CategoryView : BaseMvpView {

    fun productListLoaded(productList: List<Product>?)
}

class CategoryPresenter @Inject constructor(repository: Repository) : BasePresenter<CategoryView>(repository) {

    fun loadProductList(perPage: Int, paginationValue: String?, categoryId: String, sortType: SortType) {

        showProgress()

        //TODO MOVE TO SHOPIFY
        val reverse = sortType == SortType.RECENT

        disposables.add(repository.getCategory(categoryId, perPage, paginationValue, sortType, reverse)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (isViewAttached) {
                        view.productListLoaded(result.productList)
                        view.hideProgress()
                    }
                }, { error ->
                    error.printStackTrace()
                    hideProgress()
                }))
    }
}