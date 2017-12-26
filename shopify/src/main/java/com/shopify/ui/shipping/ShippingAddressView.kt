package com.shopify.ui.shipping

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.domain.entity.Address
import com.shopify.api.R
import kotlinx.android.synthetic.main.view_shipping_address.view.*

class ShippingAddressView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) :
        ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_shipping_address, this)
        setBackgroundColor(Color.WHITE)
    }

    fun setAddress(address: Address?) {
        if (address != null) {
            addressContent.setAddress(address)
            addressContent.visibility = View.VISIBLE
            editButton.visibility = View.VISIBLE
            addNewAddressButton.visibility = View.GONE
        } else {
            editButton.visibility = View.GONE
            addNewAddressButton.visibility = View.VISIBLE
            addressContent.visibility = View.GONE
        }
    }

    fun setClickListeners(editClickListener: OnClickListener, addAddressClickListener: OnClickListener) {
        editButton.setOnClickListener(editClickListener)
        addNewAddressButton.setOnClickListener(addAddressClickListener)
    }
}
