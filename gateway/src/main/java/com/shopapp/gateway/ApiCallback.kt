package com.shopapp.gateway

import com.shopapp.gateway.entity.Error

interface ApiCallback<in T> {

    fun onResult(result: T)

    fun onFailure(error: Error)
}
