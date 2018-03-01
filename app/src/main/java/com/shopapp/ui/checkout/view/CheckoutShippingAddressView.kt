package com.shopapp.ui.checkout.view

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.shopapp.R
import com.shopapp.gateway.entity.Address
import kotlinx.android.synthetic.main.view_shipping_address.view.*

class CheckoutShippingAddressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ConstraintLayout(context, attrs, defStyleAttr) {

    private var address: Address? = null

    init {
        View.inflate(context, R.layout.view_shipping_address, this)
        setBackgroundColor(Color.WHITE)
    }

    fun setAddress(address: Address?) {
        this.address = address
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

    fun getAddress() = address

    fun setClickListeners(editClickListener: OnClickListener, addAddressClickListener: OnClickListener) {
        editButton.setOnClickListener(editClickListener)
        addNewAddressButton.setOnClickListener(addAddressClickListener)
    }
}
