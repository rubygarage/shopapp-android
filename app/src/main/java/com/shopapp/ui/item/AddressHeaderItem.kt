package com.shopapp.ui.item

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import com.shopapp.R
import kotlinx.android.synthetic.main.item_address_header.view.*

class AddressHeaderItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var headerClickListener: OnClickListener? = null
        set(value) {
            addNewAddressButton.setOnClickListener(value)
        }

    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.item_address_header, this)
    }
}