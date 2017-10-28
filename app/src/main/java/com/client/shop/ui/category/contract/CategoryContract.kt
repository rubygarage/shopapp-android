package com.client.shop.ui.category.contract

import com.client.shop.ui.base.contract.BaseMvpViewLce
import com.client.shop.ui.base.contract.BasePresenterLce
import com.domain.entity.Product
import com.domain.entity.SortType
import com.repository.Repository
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

interface CategoryView : BaseMvpViewLce<List<Product>>

class CategoryPresenter @Inject constructor(repository: Repository) : BasePresenterLce<List<Product>, CategoryView>(repository) {

    fun loadProductList(perPage: Int, paginationValue: String?, categoryId: String, sortType: SortType) {

        //TODO MOVE TO SHOPIFY
        val reverse = sortType == SortType.RECENT

        disposables.add(repository.getCategory(categoryId, perPage, paginationValue, sortType, reverse)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result -> view?.showContent(result.productList) },
                        { error ->
                            error.printStackTrace()
                        }))
    }
}