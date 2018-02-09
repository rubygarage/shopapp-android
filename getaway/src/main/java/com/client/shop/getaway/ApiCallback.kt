package com.client.shop.getaway

import com.client.shop.getaway.entity.Error

interface ApiCallback<in T> {

    fun onResult(result: T)

    fun onFailure(error: Error)
}
