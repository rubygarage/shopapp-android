package com.client.shop.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.details.contract.OrderDetailsPresenter
import com.client.shop.ui.details.contract.OrderDetailsView
import com.client.shop.ui.order.details.adapter.OrderProductsAdapter
import com.client.shop.ui.order.di.OrderModule
import com.domain.entity.Order
import com.domain.formatter.DateFormatter
import com.domain.formatter.NumberFormatter
import com.ui.base.lce.BaseActivity
import kotlinx.android.synthetic.main.activity_order_details.*
import java.math.BigDecimal
import javax.inject.Inject

class OrderDetailsActivity :
        BaseActivity<Order, OrderDetailsView, OrderDetailsPresenter>(),
        OrderDetailsView {

    companion object {
        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"

        fun getStartIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(EXTRA_ORDER_ID, orderId)
            return intent
        }
    }

    @Inject lateinit var detailsPresenter: OrderDetailsPresenter
    private lateinit var orderId: String
    private lateinit var formatter: NumberFormatter

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = intent.getStringExtra(EXTRA_ORDER_ID)
        formatter = NumberFormatter()
        loadData()
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachOrderComponent(OrderModule()).inject(this)
    }

    override fun getContentView() = R.layout.activity_order_details

    override fun createPresenter(): OrderDetailsPresenter {
        return detailsPresenter
    }

    //LCE

    override fun loadData(pullToRefresh: Boolean) {
        super.loadData(pullToRefresh)
        presenter.loadOrderDetails(orderId)
    }

    override fun showContent(data: Order) {
        super.showContent(data)

        orderTitleView.setOrder(data, DateFormatter())
        priceView.setData(
                data.subtotalPrice ?: BigDecimal.ZERO,
                BigDecimal.ZERO,
                data.totalShippingPrice ?: BigDecimal.ZERO,
                data.totalPrice,
                data.currency)
        data.address?.let {
            addressContentView.setAddress(it)
        }

        productRecyclerView.adapter = OrderProductsAdapter(data.orderProducts, data.currency)
        productRecyclerView.layoutManager = LinearLayoutManager(this)

    }
}