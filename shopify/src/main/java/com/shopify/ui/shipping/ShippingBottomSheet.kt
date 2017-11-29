package com.shopify.ui.shipping

import android.content.Context
import android.support.design.widget.BottomSheetDialog
import android.widget.TextView
import com.shopify.api.R
import com.shopify.entity.ShippingRate
import kotlinx.android.synthetic.main.bottom_sheet_shipping.*

class ShippingBottomSheet(context: Context, shippingRates: List<ShippingRate>, listener: OnShippingRateSelectListener) : BottomSheetDialog(context) {

    init {
        setContentView(R.layout.bottom_sheet_shipping)
        for (value in shippingRates) {
            val itemView = layoutInflater.inflate(R.layout.item_shipping, containerLayout, false) as TextView
            itemView.text = value.title
            itemView.setOnClickListener {
                listener.onShippingRateSelected(value)
                dismiss()
            }
            containerLayout.addView(itemView)
        }
    }

    interface OnShippingRateSelectListener {

        fun onShippingRateSelected(shippingRate: ShippingRate)
    }
}