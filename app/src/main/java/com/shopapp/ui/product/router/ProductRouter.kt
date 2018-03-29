package com.shopapp.ui.product.router

import android.content.Context
import com.shopapp.gateway.entity.SortType
import com.shopapp.ui.product.ProductDetailsActivity
import com.shopapp.ui.product.ProductListActivity

class ProductRouter {

    fun showProduct(context: Context?, id: String) {
        context?.let { it.startActivity(ProductDetailsActivity.getStartIntent(it, id)) }
    }

    fun showProductList(context: Context?, title: String, sortType: SortType = SortType.NAME,
                        keyword: String? = null, excludeKeyword: String? = null) {
        context?.let { it.startActivity(ProductListActivity.getStartIntent(it, title, sortType, keyword, excludeKeyword)) }
    }
}