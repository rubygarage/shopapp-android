package com.client.shop.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import com.client.shop.R
import kotlinx.android.synthetic.main.item_more.view.*

@SuppressLint("ViewConstructor")
class MoreItem(context: Context,
               width: Int = LayoutParams.MATCH_PARENT,
               height: Int = LayoutParams.MATCH_PARENT) : FrameLayout(context) {

    val moreButton: Button

    init {
        layoutParams = LayoutParams(width, height)
        View.inflate(context, R.layout.item_more, this)
        moreButton = button
    }
}