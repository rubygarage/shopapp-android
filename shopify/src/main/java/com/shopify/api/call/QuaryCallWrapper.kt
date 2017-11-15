package com.shopify.api.call

import com.apicore.ApiCallback
import com.domain.entity.Error
import com.shopify.buy3.*

abstract class QuaryCallWrapper<out T>(private val callback: ApiCallback<T>) : GraphCall.Callback<Storefront.QueryRoot> {

    internal abstract fun adapt(data: Storefront.QueryRoot): T

    override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
        response.data()?.let {
            callback.onResult(adapt(it))
        }
    }

    override fun onFailure(graphError: GraphError) {
        val error: Error = when (graphError) {
            is GraphNetworkError -> Error.Content(isNetworkError = true)
            else -> Error.Content()
        }
        callback.onFailure(error)
    }
}
