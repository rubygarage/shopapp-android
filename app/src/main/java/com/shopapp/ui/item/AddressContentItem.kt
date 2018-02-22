package com.shopapp.ui.item

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.shopapp.gateway.entity.Address
import com.shopapp.R
import kotlinx.android.synthetic.main.item_address_content.view.*

class AddressContentItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.item_address_content, this)
        orientation = VERTICAL
    }

    fun setAddress(address: Address) {
        name.text = context.getString(R.string.full_name_pattern, address.firstName, address.lastName)
        addressText.text = ""
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
            phone.text = context.getString(R.string.tel_prefix, address.phone)
        }
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

