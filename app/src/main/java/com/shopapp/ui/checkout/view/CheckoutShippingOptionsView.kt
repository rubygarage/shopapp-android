package com.shopapp.ui.checkout.view

import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.shopapp.R
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.ShippingRate
import com.shopapp.ui.checkout.adapter.ShippingOptionsAdapter
import kotlinx.android.synthetic.main.view_checkout_shipping_options.view.*

class CheckoutShippingOptionsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    var onOptionSelectedListener: OnOptionSelectedListener? = null
        set(value) {
            value?.let { adapter.onOptionSelectedListener = value }
        }
    private val adapter: ShippingOptionsAdapter

    init {
        View.inflate(context, R.layout.view_checkout_shipping_options, this)
        adapter = ShippingOptionsAdapter(mutableListOf(), NumberFormatter())
        recyclerView.isNestedScrollingEnabled = false
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    fun setData(checkout: Checkout, shippingRates: List<ShippingRate>) {
        if (shippingRates.isEmpty()) {
            title.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary))
            message.setText(R.string.order_can_not_be_processed)
            message.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            title.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary))
            message.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter.setData(checkout, shippingRates)
            adapter.notifyDataSetChanged()
        }
    }

    fun unSelectAddress() {
        title.setTextColor(ContextCompat.getColor(context, R.color.textColorSecondary))
        message.setText(R.string.please_add_shipping_address_first)
        message.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }

    fun selectShippingRate(shippingRate: ShippingRate) {
        adapter.shippingRate = shippingRate
        adapter.notifyDataSetChanged()
    }

    interface OnOptionSelectedListener {

        fun onOptionSelected(shippingRate: ShippingRate)
    }
}