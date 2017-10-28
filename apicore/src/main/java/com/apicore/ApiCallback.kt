package com.apicore

import com.domain.entity.Error

interface ApiCallback<in T> {

    fun onResult(result: T)

    fun onFailure(error: Error)
}
