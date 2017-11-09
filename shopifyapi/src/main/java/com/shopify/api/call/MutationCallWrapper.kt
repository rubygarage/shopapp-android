package com.shopify.api.call

import com.apicore.ApiCallback
import com.domain.entity.Error
import com.shopify.buy3.*

abstract class MutationCallWrapper<out T>(private val callback: ApiCallback<T>) : GraphCall.Callback<Storefront.Mutation> {

    internal abstract fun adapt(data: Storefront.Mutation?): T?

    override fun onResponse(response: GraphResponse<Storefront.Mutation>) {
        val result = adapt(response.data())
        if (result != null) {
            callback.onResult(result)
        } else {
            callback.onFailure(Error.Content())
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