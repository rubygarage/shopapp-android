package com.client.shop.ui.search

import android.os.Bundle
import com.client.shop.ui.search.contract.SearchView
import com.hannesdorfmann.mosby3.mvp.viewstate.RestorableViewState
import com.shopapicore.entity.Product

class SearchViewState : RestorableViewState<SearchView> {

    private var data: List<Product>? = null
    private var query: String = ""

    companion object {
        private const val KEY_PRODUCT_LIST = "SearchViewState-product-list"
        private const val KEY_PRODUCT_QUERY = "SearchViewState-product-query"
    }

    override fun restoreInstanceState(`in`: Bundle?): RestorableViewState<SearchView>? {
        if (`in` == null) {
            return null
        }

        data = `in`.getParcelableArrayList(KEY_PRODUCT_LIST)
        query = `in`.getString(KEY_PRODUCT_QUERY, "")

        return this
    }

    override fun saveInstanceState(out: Bundle) {
        if (data != null) {
            out.putParcelableArrayList(KEY_PRODUCT_LIST, ArrayList<Product>(data))
            out.putString(KEY_PRODUCT_QUERY, query)
        }
    }

    override fun apply(view: SearchView?, retained: Boolean) {
        data?.let {
            view?.searchResultsReceived(it, query)
            view?.hideProgress()
        }
    }

    fun setSearchData(data: List<Product>, query: String) {
        this.data = data
        this.query = query
    }
}