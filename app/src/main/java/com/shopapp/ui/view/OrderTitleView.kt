package com.shopapp.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import com.shopapp.gateway.entity.Order
import com.shopapp.domain.formatter.DateFormatter
import com.shopapp.R
import kotlinx.android.synthetic.main.view_order_title.view.*

class OrderTitleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) :
    LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_order_title, this)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = VERTICAL
    }

    fun setOrder(order: Order, dateFormatter: DateFormatter) {
        orderNumberTextView.text = resources.getString(R.string.order_number_pattern,
            order.orderNumber).toUpperCase()
        orderDateTextView.text = resources.getString(R.string.order_date_pattern,
            dateFormatter.format(order.processedAt))
    }
}