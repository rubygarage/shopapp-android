package com.shopapp.ui.order.list.contract

import com.shopapp.domain.interactor.order.GetOrdersUseCase
import com.shopapp.gateway.entity.Order
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface OrderListView : BaseLceView<List<Order>>

class OrderListPresenter @Inject constructor(private val getOrdersUseCase: GetOrdersUseCase) :
    BaseLcePresenter<List<Order>, OrderListView>(getOrdersUseCase) {

    fun getOrders(perPage: Int, paginationValue: String?) {

        getOrdersUseCase.execute(
            {
                if (paginationValue == null && it.isEmpty()) {
                    view?.showEmptyState()
                } else {
                    view?.showContent(it)
                }
            },
            { resolveError(it) },
            GetOrdersUseCase.Params(perPage, paginationValue)
        )
    }
}