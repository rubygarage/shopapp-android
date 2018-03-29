package com.shopapp.ui.order.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.shopapp.R
import com.shopapp.ShopApplication
import com.shopapp.domain.formatter.DateFormatter
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.Order
import com.shopapp.ui.base.lce.BaseLceActivity
import com.shopapp.ui.base.recycler.OnItemClickListener
import com.shopapp.ui.base.recycler.divider.SpaceDecoration
import com.shopapp.ui.order.details.adapter.OrderProductsAdapter
import com.shopapp.ui.order.details.contract.OrderDetailsPresenter
import com.shopapp.ui.order.details.contract.OrderDetailsView
import com.shopapp.ui.order.details.router.OrderRouter
import com.shopapp.ui.product.ProductDetailsActivity
import kotlinx.android.synthetic.main.activity_lce.*
import kotlinx.android.synthetic.main.activity_order_details.*
import kotlinx.android.synthetic.main.layout_lce.view.*
import java.math.BigDecimal
import javax.inject.Inject

class OrderDetailsActivity :
    BaseLceActivity<Order, OrderDetailsView, OrderDetailsPresenter>(),
    OrderDetailsView, OnItemClickListener {

    private var order: Order? = null

    companion object {
        const val EXTRA_ORDER_ID = "EXTRA_ORDER_ID"

        fun getStartIntent(context: Context, orderId: String): Intent {
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(EXTRA_ORDER_ID, orderId)
            return intent
        }
    }

    @Inject
    lateinit var detailsPresenter: OrderDetailsPresenter

    @Inject
    lateinit var router: OrderRouter

    private lateinit var orderId: String
    private lateinit var formatter: NumberFormatter

    //ANDROID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderId = intent.getStringExtra(EXTRA_ORDER_ID)
        formatter = NumberFormatter()
        setTitle(getString(R.string.order_details))
        lceLayout.errorView.errorTarget = getString(R.string.order)

        loadData()
    }

    //INITIAL

    override fun inject() {
        ShopApplication.appComponent.attachOrderComponent().inject(this)
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

        val address = data.address
        if (address != null) {
            addressContentView.setAddress(address)
        } else {
            addressContentView.visibility = View.GONE
            shippingAddressTitle.visibility = View.GONE
        }

        val spaceDecoration = SpaceDecoration(topSpace = resources.getDimensionPixelSize(R.dimen.order_details_product_item_vertical_margin))
        recyclerView.addItemDecoration(spaceDecoration)
        recyclerView.adapter = OrderProductsAdapter(data.orderProducts, this, data.currency)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
    }

    override fun onItemClicked(position: Int) {
        val productVariant = order?.let { it.orderProducts.getOrNull(position)?.productVariant }
        productVariant?.let { router.showProduct(this, it) }
    }
}