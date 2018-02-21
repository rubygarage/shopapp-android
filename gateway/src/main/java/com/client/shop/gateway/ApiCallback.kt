package com.client.shop.gateway

import com.client.shop.gateway.entity.Error

interface ApiCallback<in T> {

    fun onResult(result: T)

    fun onFailure(error: Error)
}
