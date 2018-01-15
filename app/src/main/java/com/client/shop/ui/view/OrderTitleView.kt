package com.client.shop.ui.view

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.view.View
import com.client.shop.R
import com.domain.entity.Order
import com.domain.formatter.DateFormatter
import kotlinx.android.synthetic.main.view_order_title.view.*

class OrderTitleView(context: Context,
                     attrs: AttributeSet? = null,
                     defStyleAttr: Int = 0) :
        LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        View.inflate(context, R.layout.view_order_title, this)
    }

    fun setOrder(order: Order, dateFormatter: DateFormatter) {
        orderNumberTextView.text = resources.getString(R.string.order_number_pattern,
                order.orderNumber)
        orderDateTextView.text = resources.getString(R.string.order_date_pattern,
                dateFormatter.format(order.processedAt))
    }
}