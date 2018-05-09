package com.shopapp.ui.item

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.shopapp.R
import com.shopapp.gateway.entity.Category

@SuppressLint("ViewConstructor")
class CategoryItem(context: Context, isGridMode: Boolean = true) : ConstraintLayout(context) {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        val layout = if (isGridMode) R.layout.item_category_grid else R.layout.item_category
        inflate(context, layout, this)
        setBackgroundResource(R.color.white)
    }

    fun setCategory(category: Category) {
        findViewById<TextView>(R.id.titleText).text = category.title
        findViewById<SimpleDraweeView?>(R.id.image)?.setImageURI(category.image?.src)
    }
}