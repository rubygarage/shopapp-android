package com.shopapp.ui.search.router

import android.content.Context
import com.shopapp.ui.product.ProductDetailsActivity

class SearchRouter {

    fun showProduct(context: Context?, id: String) {
        context?.let { it.startActivity(ProductDetailsActivity.getStartIntent(it, id)) }
    }

}