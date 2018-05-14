package com.shopapp.ui.item.category

import android.content.Context
import com.shopapp.R
import com.shopapp.gateway.entity.Category
import kotlinx.android.synthetic.main.item_category_grid.view.*

class CategoryGridItem(context: Context) : BaseCategoryItem(context) {

    override fun getLayout() = R.layout.item_category_grid

    override fun setCategory(category: Category) {
        titleText.text = category.title
        image.setImageURI(category.image?.src)
    }
}