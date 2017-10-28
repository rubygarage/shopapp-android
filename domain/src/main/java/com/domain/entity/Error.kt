package com.domain.entity

sealed class Error : Exception() {
    class Critical : Error()
    class Content(var isNetworkError: Boolean = false) : Error()
}