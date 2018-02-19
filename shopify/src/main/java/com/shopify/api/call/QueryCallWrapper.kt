package com.shopify.api.call

import com.client.shop.gateway.ApiCallback
import com.client.shop.gateway.entity.Error
import com.shopify.api.adapter.ErrorAdapter
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront

abstract class QueryCallWrapper<out T>(private val callback: ApiCallback<T>) : GraphCall.Callback<Storefront.QueryRoot> {

    internal abstract fun adapt(data: Storefront.QueryRoot): T?

    override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
        val error = ErrorAdapter.adaptErrors(response.errors())
        val data = response.data()
        val result = data?.let { adapt(it) }
        when {
            error != null -> callback.onFailure(error)
            result != null -> callback.onResult(result)
            else -> callback.onFailure(Error.Content())
        }
    }

    override fun onFailure(graphError: GraphError) {
        callback.onFailure(ErrorAdapter.adapt(graphError))
    }
}
