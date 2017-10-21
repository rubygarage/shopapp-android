package com.shopify.api

import com.apicore.ApiCallback
import com.shopify.buy3.GraphCall
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphResponse
import com.shopify.buy3.Storefront

abstract class CallWrapper<out T>(private val callback: ApiCallback<T>) : GraphCall.Callback<Storefront.QueryRoot> {

    internal abstract fun adapt(data: Storefront.QueryRoot): T

    override fun onResponse(response: GraphResponse<Storefront.QueryRoot>) {
        response.data()?.let {
            callback.onResult(adapt(it))
        }
    }

    override fun onFailure(error: GraphError) {
        callback.onFailure(Error(error))
    }
}
