package com.shopapp.ui.category.router

import android.content.Context
import com.shopapp.gateway.entity.Category
import com.shopapp.gateway.entity.ProductVariant
import com.shopapp.ui.category.CategoryActivity
import com.shopapp.ui.product.ProductDetailsActivity

class CategoryListRouter {

    fun showCategory(context: Context?, category: Category) {
        context?.let { it.startActivity(CategoryActivity.getStartIntent(it, category)) }
    }

}