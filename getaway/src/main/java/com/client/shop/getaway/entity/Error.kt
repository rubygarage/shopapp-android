package com.client.shop.getaway.entity

sealed class Error : Exception() {
    class Critical : Error()
    class NonCritical(override val message: String) : Error()
    class Content(var isNetworkError: Boolean = false) : Error()
}