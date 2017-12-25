package com.shopify.ui.shipping

import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.text.TextUtils
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
            name.visibility = View.VISIBLE
            addressText.visibility = View.VISIBLE
            editButton.visibility = View.VISIBLE
            addNewAddressButton.visibility = View.GONE

            name.text = context.getString(R.string.full_name_pattern, address.firstName, address.lastName)
            appendAddress(address.address)
            appendAddress(address.secondAddress)
            appendAddress(address.city)
            appendAddress(address.state)
            appendAddress(address.zip)
            appendAddress(address.country)

            if (TextUtils.isEmpty(address.phone)) {
                phone.visibility = View.GONE
            } else {
                phone.visibility = View.VISIBLE
                phone.text = address.phone
            }
        } else {
            name.visibility = View.GONE
            addressText.visibility = View.GONE
            editButton.visibility = View.GONE
            phone.visibility = View.GONE
            addNewAddressButton.visibility = View.VISIBLE
        }
    }

    fun setClickListeners(editClickListener: OnClickListener, addAddressClickListener: OnClickListener) {
        editButton.setOnClickListener(editClickListener)
        addNewAddressButton.setOnClickListener(addAddressClickListener)
    }

    private fun appendAddress(str: String?) {
        str?.let {
            if (it.isNotEmpty()) {
                val separator = if (addressText.text.isEmpty()) "" else ", "
                addressText.append(separator)
                addressText.append(str)
            }
        }
    }
}
