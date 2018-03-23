package com.shopapp.ui.checkout.adapter

import android.content.Context
import android.view.View
import com.shopapp.domain.formatter.NumberFormatter
import com.shopapp.gateway.entity.Checkout
import com.shopapp.gateway.entity.ShippingRate
import com.shopapp.ui.base.recycler.adapter.BaseRecyclerAdapter
import com.shopapp.ui.checkout.view.CheckoutShippingOptionsView
import com.shopapp.ui.item.ShippingOptionItem

class ShippingOptionsAdapter(
    private val data: MutableList<ShippingRate>,
    private val numberFormatter: NumberFormatter
) : BaseRecyclerAdapter<ShippingRate>(data) {

    var shippingRate: ShippingRate? = null
    var onOptionSelectedListener: CheckoutShippingOptionsView.OnOptionSelectedListener? = null
    private var currencyCode: String? = null

    override fun getItemView(context: Context, viewType: Int): View =
        ShippingOptionItem(context, onOptionSelectedListener)

    override fun bindData(itemView: View, data: ShippingRate, position: Int) {
        val shippingRate = this.shippingRate
        val currencyCode = this.currencyCode
        if (itemView is ShippingOptionItem && currencyCode != null) {
            itemView.setData(data, shippingRate, currencyCode, numberFormatter)
        }
    }

    fun setData(checkout: Checkout, data: List<ShippingRate>) {
        this.shippingRate = checkout.shippingRate
        this.currencyCode = checkout.currency
        this.data.clear()
        this.data.addAll(data)
    }
}