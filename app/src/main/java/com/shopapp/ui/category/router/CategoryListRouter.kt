package com.shopapp.ui.category.router

import android.content.Context
import com.shopapp.gateway.entity.Category
import com.shopapp.ui.category.CategoryActivity

class CategoryListRouter {

    fun showCategory(context: Context?, category: Category) {
        context?.let { it.startActivity(CategoryActivity.getStartIntent(it, category)) }
    }

}