package com.client.shop.ui.category

import android.os.Bundle
import com.client.shop.ui.category.contract.CategoryView
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState
import com.shopapicore.entity.Product

class CategoryViewState : RestorableViewState<CategoryView> {

    private var data: List<Product>? = null

    companion object {
        private const val KEY_PRODUCT_LIST = "CategoryViewState-product-list"
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<CategoryView>? {
        if (`in` == null) {
            return null
        }

        data = `in`.getParcelableArrayList(KEY_PRODUCT_LIST)

        return this
    }

    override fun saveInstanceState(out: Bundle) {
        if (data != null) {
            out.putParcelableArrayList(KEY_PRODUCT_LIST, ArrayList<Product>(data))
        }
    }

    override fun apply(view: CategoryView?, retained: Boolean) {
        data?.let {
            view?.productListLoaded(it)
            view?.hideProgress()
        }
    }

    fun setData(data: List<Product>) {
        this.data = data
    }
}