package com.client.shop.ui.recent

import android.os.Bundle
import com.client.shop.ui.recent.contract.RecentView
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState
import com.shopapicore.entity.Product

class RecentViewState : RestorableViewState<RecentView> {

    private var data: List<Product>? = null

    companion object {
        private const val KEY_PRODUCT_LIST = "RecentViewState-product-list"
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<RecentView>? {
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

    override fun apply(view: RecentView?, retained: Boolean) {
        data?.let {
            view?.productListLoaded(it)
            view?.hideProgress()
        }
    }

    fun setData(data: List<Product>) {
        this.data = data
    }
}