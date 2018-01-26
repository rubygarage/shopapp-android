package com.client.shop.ui.order.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.ui.pagination.PaginationActivity
import com.client.shop.ui.order.details.OrderDetailsActivity
import com.client.shop.ui.order.di.OrderModule
import com.client.shop.ui.order.list.adapter.OrderAdapter
import com.client.shop.ui.order.list.contract.OrderListPresenter
import com.client.shop.ui.order.list.contract.OrderListView
import com.client.shop.ui.product.ProductDetailsActivity
import com.domain.entity.Order
import com.ui.base.recycler.divider.BackgroundItemDecoration
import com.ui.base.recycler.divider.SpaceDecoration
import kotlinx.android.synthetic.main.activity_order_list.*
import javax.inject.Inject

class OrderListActivity :
    PaginationActivity<Order, OrderListView, OrderListPresenter>(),
    OrderListView, OrderAdapter.OnOrderItemClickListener {

    @Inject
    lateinit var orderListPresenter: OrderListPresenter

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
        ShopApplication.appComponent.attachOrderComponent(OrderModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_order_list

    override fun createPresenter() = orderListPresenter

    //SETUP

    override fun setupAdapter(): OrderAdapter {
        return OrderAdapter(dataList, this)
    }

    override fun setupRecyclerView() {
        super.setupRecyclerView()
        val spaceDecoration = SpaceDecoration(topSpace = resources.getDimensionPixelSize(R.dimen.order_item_vertical_margin))
        recyclerView.addItemDecoration(spaceDecoration)
        recyclerView.addItemDecoration(BackgroundItemDecoration(R.color.white))
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
    }

    override fun onItemClicked(data: Order, position: Int) {
        startActivity(OrderDetailsActivity.getStartIntent(this, data.id))
    }

    override fun onProductVariantClicked(orderPosition: Int, productPosition: Int) {
        val productVariant = dataList.getOrNull(orderPosition)
            ?.orderProducts
            ?.getOrNull(productPosition)
            ?.productVariant
        productVariant?.let {
            startActivity(ProductDetailsActivity.getStartIntent(this, it))
        }
    }
}