package com.client.shop.ui.recent.contract

import com.domain.entity.Product
import com.domain.interactor.recent.RecentUseCase
import com.ui.base.contract.BasePresenter
import com.ui.base.contract.BaseView
import javax.inject.Inject

interface RecentView : BaseView<List<Product>>

class RecentPresenter @Inject constructor(private val recentUseCase: RecentUseCase) :
        BasePresenter<List<Product>, RecentView>(arrayOf(recentUseCase)) {

    fun loadProductList(perPage: Int, paginationValue: String? = null) {

        recentUseCase.execute(
                { view?.showContent(it) },
                { it.printStackTrace() },
                RecentUseCase.Params(perPage, paginationValue)
        )
    }
}