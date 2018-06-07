package com.shopapp.ui.item.category

import android.content.Context
import android.support.constraint.ConstraintLayout
import com.shopapp.R
import com.shopapp.gateway.entity.Category
import kotlinx.android.synthetic.main.item_category_grid.view.*

class CategoryGridItem(context: Context) : ConstraintLayout(context) {

    init {
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        inflate(context, R.layout.item_category_grid, this)
        setBackgroundResource(R.color.white)
    }

    fun setCategory(category: Category) {
        titleText.text = category.title
        image.setImageURI(category.image?.src)
    }
}