package com.shopify.api.adapter

import com.domain.entity.Error
import com.shopify.buy3.GraphError
import com.shopify.buy3.GraphNetworkError
import com.shopify.buy3.Storefront

object ErrorAdapter {

    fun adapt(adaptee: GraphError): Error = when (adaptee) {
        is GraphNetworkError -> Error.Content(isNetworkError = true)
        else -> Error.Content()
    }

    fun adaptErrors(adaptee: List<com.shopify.graphql.support.Error>?): Error? {
        return if (adaptee != null) {
            adaptee.firstOrNull()?.let { Error.NonCritical(it.message()) }
        } else {
            null
        }
    }

    fun adaptErrors(adaptee: Throwable?): Error? {

        return adaptee?.let {
            it.message?.let { Error.NonCritical(it) }
        }
    }

    fun adaptUserError(adaptee: List<Storefront.UserError>?): Error? {
        return if (adaptee != null) {
            adaptee.firstOrNull()?.let { Error.NonCritical(it.message) }
        } else {
            null
        }
    }
}