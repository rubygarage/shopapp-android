package com.shopapp.ui.order.list.contract

import com.shopapp.domain.interactor.order.OrderListUseCase
import com.shopapp.gateway.entity.Order
import com.shopapp.ui.base.contract.BaseLcePresenter
import com.shopapp.ui.base.contract.BaseLceView
import javax.inject.Inject

interface OrderListView : BaseLceView<List<Order>>

class OrderListPresenter @Inject constructor(private val orderListUseCase: OrderListUseCase) :
    BaseLcePresenter<List<Order>, OrderListView>(orderListUseCase) {

    fun getOrders(perPage: Int, paginationValue: String?) {

        orderListUseCase.execute(
            {
                if (paginationValue == null && it.isEmpty()) {
                    view?.showEmptyState()
                } else {
                    view?.showContent(it)
                }
            },
            { resolveError(it) },
            OrderListUseCase.Params(perPage, paginationValue)
        )
    }
}