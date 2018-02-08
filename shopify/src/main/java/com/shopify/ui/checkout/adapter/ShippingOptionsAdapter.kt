package com.shopify.ui.checkout.adapter

import android.content.Context
import android.view.View
import com.domain.formatter.NumberFormatter
import com.shopify.entity.Checkout
import com.shopify.entity.ShippingRate
import com.shopify.ui.checkout.view.CheckoutShippingOptionsView
import com.shopify.ui.item.ShippingOptionItem
import com.ui.base.recycler.adapter.BaseRecyclerAdapter

class ShippingOptionsAdapter(
    private val data: MutableList<ShippingRate>
) : BaseRecyclerAdapter<ShippingRate>(data) {

    var shippingRate: ShippingRate? = null
    var onOptionSelectedListener: CheckoutShippingOptionsView.OnOptionSelectedListener? = null
    private var currencyCode: String? = null
    private val numberFormatter = NumberFormatter()

    override fun getItemView(context: Context, viewType: Int) =
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