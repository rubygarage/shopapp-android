package com.shopapp.ui.order.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.gateway.entity.Order
import com.shopapp.ui.base.lce.view.LceEmptyView
import com.shopapp.ui.base.pagination.PaginationActivity
import com.shopapp.ui.base.recycler.divider.BackgroundItemDecoration
import com.shopapp.ui.base.recycler.divider.SpaceDecoration
import com.shopapp.ui.order.list.adapter.OrderListAdapter
import com.shopapp.ui.order.list.contract.OrderListPresenter
import com.shopapp.ui.order.list.contract.OrderListView
import com.shopapp.ui.order.list.router.OrderListRouter
import kotlinx.android.synthetic.main.activity_order_list.*
import javax.inject.Inject

class OrderListActivity :
    PaginationActivity<Order, OrderListView, OrderListPresenter>(),
    OrderListView, OrderListAdapter.OnOrderItemClickListener {

    @Inject
    lateinit var orderListPresenter: OrderListPresenter

    @Inject
    lateinit var router: OrderListRouter

    companion object {

        fun getStartIntent(context: Context): Intent {
            return Intent(context, OrderListActivity::class.java)
        }
    }

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(getString(R.string.my_orders))
        loadData()
    }

    //INIT

    override fun inject() {
        ShopApplication.appComponent.attachOrderComponent().inject(this)
    }

    override fun getContentView() = R.layout.activity_order_list

    override fun createPresenter() = orderListPresenter

    //SETUP

    override fun setupAdapter(): OrderListAdapter {
        return OrderListAdapter(dataList, this)
    }

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        val spaceDecoration = SpaceDecoration(topSpace = resources.getDimensionPixelSize(R.dimen.order_item_vertical_margin))
        recyclerView.addItemDecoration(spaceDecoration)
        recyclerView.addItemDecoration(BackgroundItemDecoration(R.color.white))
    }

    override fun setupEmptyView(emptyView: LceEmptyView) {
        emptyView.customiseEmptyImage(R.drawable.ic_orders_empty)
        emptyView.customiseEmptyMessage(R.string.you_have_no_orders_yet)
        emptyView.customiseEmptyButtonText(R.string.start_shopping)
        emptyView.customiseEmptyButtonVisibility(true)
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.getOrders(perPageCount(), paginationValue)
    }

    override fun showContent(data: List<Order>) {
        super.showContent(data)
        this.dataList.addAll(data)
        adapter.notifyDataSetChanged()
        if (data.isNotEmpty()) {
            paginationValue = data.last().paginationValue
        }
        if (dataList.isEmpty()) {
            showEmptyState()
        }
    }

    //CALLBACK

    override fun onItemClicked(data: Order, position: Int) {
        router.showOrder(this, data.id)
    }

    override fun onProductVariantClicked(orderPosition: Int, productPosition: Int) {
        val productVariant = dataList.getOrNull(orderPosition)?.let {
            it.orderProducts.getOrNull(productPosition)?.productVariant
        }
        productVariant?.let {
            router.showProduct(this, it)
        }
    }

    override fun onEmptyButtonClicked() {
        router.showHome(this, true)
    }
}