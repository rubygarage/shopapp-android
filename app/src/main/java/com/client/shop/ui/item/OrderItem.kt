package com.client.shop.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import com.client.shop.R
import com.client.shop.getaway.entity.Order
import com.client.shop.ui.base.recycler.OnItemClickListener
import com.client.shop.ui.product.adapter.ProductVariantAdapter
import com.client.shop.ui.base.recycler.divider.SpaceDecoration
import com.domain.formatter.DateFormatter
import com.domain.formatter.NumberFormatter
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper
import kotlinx.android.synthetic.main.item_order.view.*

@SuppressLint("ViewConstructor")
class OrderItem(
    context: Context,
    private val dateFormatter: DateFormatter,
    private val numberFormatter: NumberFormatter) : ConstraintLayout(context
) {

    private var onProductVariantClickListener: OnProductVariantClickListener? = null

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.item_order, this)
        setupVariantRecyclerView()
    }

    fun setOrder(order: Order) {

        orderTitleView.setOrder(order, dateFormatter)

        itemsCountValueTextView.text = order.orderProducts.size.toString()
        totalPriceTextView.text = resources.getString(R.string.total_price_pattern,
            numberFormatter.formatPrice(order.totalPrice, order.currency))

        productVariantsRecyclerView.adapter = ProductVariantAdapter(
            order.orderProducts.map { it.productVariant },
            object : OnItemClickListener {
                override fun onItemClicked(position: Int) {
                    onProductVariantClickListener?.onProductVariantClicked(position)
                }
            })
    }

    fun setOnProductVariantClickListener(onProductVariantClickListener: OnProductVariantClickListener) {
        this.onProductVariantClickListener = onProductVariantClickListener
    }

    private fun setupVariantRecyclerView() {
        GravitySnapHelper(Gravity.START).attachToRecyclerView(productVariantsRecyclerView)
        val decoration = SpaceDecoration(
            topSpace = resources.getDimensionPixelSize(R.dimen.order_item_products_vertical_margin),
            bottomSpace = resources.getDimensionPixelSize(R.dimen.order_item_products_vertical_margin),
            leftSpace = resources.getDimensionPixelSize(R.dimen.order_item_products_horizontal_margin),
            rightSpace = resources.getDimensionPixelSize(R.dimen.order_item_products_horizontal_margin),
            skipFirst = false)
        productVariantsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        productVariantsRecyclerView.addItemDecoration(decoration)
    }

    interface OnProductVariantClickListener {

        fun onProductVariantClicked(position: Int)

    }
}