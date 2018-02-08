package com.ui.item

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.domain.entity.Address
import com.ui.R
import kotlinx.android.synthetic.main.item_address_content.view.*

class AddressContentItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val withName: Boolean

    init {
        View.inflate(context, R.layout.item_address_content, this)
        orientation = VERTICAL

        val a = context
            .obtainStyledAttributes(attrs, R.styleable.AddressContentItem, 0, 0)
        withName = a.getBoolean(R.styleable.AddressContentItem_withName, true)
        a.recycle()
    }

    fun setAddress(address: Address) {
        name.visibility = if (withName) {
            name.text = context.getString(R.string.full_name_pattern, address.firstName, address.lastName)
            View.VISIBLE
        } else {
            View.GONE
        }
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

