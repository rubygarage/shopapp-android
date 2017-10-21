package com.client.shop.ui.item

import android.content.Context
import android.support.v7.widget.LinearLayoutCompat
import android.util.TypedValue
import android.view.View
import com.client.shop.R
import com.domain.entity.Category
import kotlinx.android.synthetic.main.item_category.view.*

class CategoryItem(context: Context) : LinearLayoutCompat(context) {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        orientation = HORIZONTAL
        View.inflate(context, R.layout.item_category, this)

        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        setBackgroundResource(outValue.resourceId)
    }

    fun setCategory(category: Category) {
        image.setImageURI(category.image?.src)
        title.text = category.title
    }
}