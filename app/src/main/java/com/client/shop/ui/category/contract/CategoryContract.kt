package com.client.shop.ui.category.contract

import com.client.shop.ui.base.contract.BaseMvpView
import com.client.shop.ui.base.contract.BasePresenter
import com.domain.entity.Product
import com.domain.entity.SortType
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface CategoryView : BaseMvpView<List<Product>>

class CategoryPresenter @Inject constructor(repository: Repository) : BasePresenter<List<Product>, CategoryView>(repository) {

    fun loadProductList(perPage: Int, paginationValue: String?, categoryId: String, sortType: SortType) {

        //TODO MOVE TO SHOPIFY
        val reverse = sortType == SortType.RECENT

        val categoryDisposable = repository.getCategory(categoryId, perPage, paginationValue, sortType, reverse)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    if (result.productList.isNotEmpty()) {
                        view?.showContent(result.productList)
                    } else {
                        view?.showEmptyState()
                    }
                },
                        { error ->
                            error.printStackTrace()
                        })

        disposables.add(categoryDisposable)
    }
}