package com.client.shop.ui.order.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.client.shop.R
import com.client.shop.ShopApplication
import com.client.shop.ui.base.lce.BaseLceActivity
import com.client.shop.ui.order.details.adapter.OrderProductsAdapter
import com.client.shop.ui.order.details.contract.OrderDetailsPresenter
import com.client.shop.ui.order.details.contract.OrderDetailsView
import com.client.shop.ui.order.di.OrderModule
import com.client.shop.ui.product.ProductDetailsActivity
import com.client.shop.getaway.entity.Order
import com.domain.formatter.DateFormatter
import com.domain.formatter.NumberFormatter
import com.client.shop.ui.base.recycler.OnItemClickListener
import com.client.shop.ui.base.recycler.divider.SpaceDecoration
import kotlinx.android.synthetic.main.activity_order_details.*
import java.math.BigDecimal
import javax.inject.Inject

class OrderDetailsActivity :
    BaseLceActivity<Order, OrderDetailsView, OrderDetailsPresenter>(),
    OrderDetailsView, OnItemClickListener {

    private var order: Order? = null

    companion object {
        private const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"

        fun getStartIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(EXTRA_ORDER_ID, orderId)
            return intent
        }
    }

    @Inject
    lateinit var detailsPresenter: OrderDetailsPresenter
    private lateinit var orderId: String
    private lateinit var formatter: NumberFormatter

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = intent.getStringExtra(EXTRA_ORDER_ID)
        formatter = NumberFormatter()
        setTitle(getString(R.string.order_details))
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
        this.order = data
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

        val spaceDecoration = SpaceDecoration(
            topSpace = resources.getDimensionPixelSize(R.dimen.order_details_product_item_vertical_margin)
        )

        recyclerView.addItemDecoration(spaceDecoration)
        recyclerView.adapter = OrderProductsAdapter(data.orderProducts, this, data.currency)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
    }

    override fun onItemClicked(position: Int) {
        val productVariant = order?.orderProducts?.getOrNull(position)?.productVariant
        productVariant?.let { startActivity(ProductDetailsActivity.getStartIntent(this, it)) }
    }
}