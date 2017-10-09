package com.client.shop.ui.item

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.client.shop.R
import kotlinx.android.synthetic.main.item_drawer.view.*

class DrawerItem @JvmOverloads constructor(context: Context,
                                           attrs: AttributeSet? = null,
                                           defStyleAttr: Int = 0) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    init {
        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        View.inflate(context, R.layout.item_drawer, this)

        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)

        val a = context.obtainStyledAttributes(attrs, R.styleable.DrawerItem, 0, 0)

        val imageId = a.getResourceId(R.styleable.DrawerItem_src, 0)
        val titleText = a.getString(R.styleable.DrawerItem_title)

        a.recycle()

        if (imageId != 0) {
            image.setImageResource(imageId)
            image.visibility = View.VISIBLE
        } else {
            image.visibility = View.GONE
        }

        title.text = titleText

        layoutParams = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)

        image.setImageResource(imageId)
    }

    fun setTitle(titleText: String) {
        title.text = titleText
    }
}