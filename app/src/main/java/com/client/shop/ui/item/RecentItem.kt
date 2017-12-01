package com.client.shop.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.widget.CardView
import android.view.View
import com.client.shop.R
import com.domain.entity.Product
import com.domain.formatter.NumberFormatter
import kotlinx.android.synthetic.main.item_recent.view.*

@SuppressLint("ViewConstructor")
class RecentItem(context: Context, private val formatter: NumberFormatter) : CardView(context) {

    init {
        View.inflate(context, R.layout.item_recent, this)
        val size = resources.getDimensionPixelSize(R.dimen.recent_item_size)
        layoutParams = LayoutParams(size, size)
        useCompatPadding = true
        preventCornerOverlap = true
    }

    fun setProduct(product: Product) {
        title.text = product.title
        price.text = formatter.formatPrice(product.price, product.currency)
        if (product.images.isNotEmpty()) {
            image.setImageURI(product.images[0].src)
        }
    }
}