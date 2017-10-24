package com.apicore

interface ApiCallback<in T> {

    fun onResult(result: T)

    fun onFailure(error: Error)
}
