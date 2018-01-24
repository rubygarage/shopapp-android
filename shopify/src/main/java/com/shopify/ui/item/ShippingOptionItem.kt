package com.shopify.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.domain.formatter.NumberFormatter
import com.shopify.api.R
import com.shopify.entity.ShippingRate
import com.shopify.ui.checkout.view.ShippingOptionsView
import kotlinx.android.synthetic.main.item_shipping_option.view.*

@SuppressLint("ViewConstructor")
class ShippingOptionItem(
    context: Context,
    private val onOptionSelectedListener: ShippingOptionsView.OnOptionSelectedListener?
) : ConstraintLayout(context) {

    init {
        View.inflate(context, R.layout.item_shipping_option, this)
        layoutParams = LayoutParams(
            MATCH_PARENT,
            resources.getDimensionPixelSize(R.dimen.shipping_options_item_height)
        )
        setBackgroundResource(R.drawable.background_white_with_bottom_divider)
    }

    fun setData(
        shippingRate: ShippingRate,
        selectedShippingRate: ShippingRate?,
        currencyCode: String,
        numberFormatter: NumberFormatter
    ) {
        radioButton.setOnCheckedChangeListener(null)
        radioButton.isChecked = shippingRate == selectedShippingRate
        radioButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                onOptionSelectedListener?.onOptionSelected(shippingRate)
            }
        }
        price.text = numberFormatter.formatPrice(shippingRate.price, currencyCode)
        name.text = shippingRate.title
    }
}