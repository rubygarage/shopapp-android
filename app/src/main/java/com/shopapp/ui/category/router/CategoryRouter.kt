package com.shopapp.ui.category.router

import android.content.Context
import com.shopapp.ui.product.ProductDetailsActivity

class CategoryRouter {

    fun showProduct(context: Context?, id: String) {
        context?.let { it.startActivity(ProductDetailsActivity.getStartIntent(it, id)) }
    }

}