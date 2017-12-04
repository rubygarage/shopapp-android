package com.client.shop.ui.recent.contract

import com.domain.entity.Product
import com.domain.interactor.recent.RecentUseCase
import com.ui.base.contract.BaseLcePresenter
import com.ui.base.contract.BaseLceView
import javax.inject.Inject

interface RecentView : BaseLceView<List<Product>>

class RecentPresenter @Inject constructor(private val recentUseCase: RecentUseCase) :
        BaseLcePresenter<List<Product>, RecentView>(recentUseCase) {

    fun loadProductList(perPage: Int, paginationValue: String? = null) {

        recentUseCase.execute(
                { view?.showContent(it) },
                { it.printStackTrace() },
                RecentUseCase.Params(perPage, paginationValue)
        )
    }
}