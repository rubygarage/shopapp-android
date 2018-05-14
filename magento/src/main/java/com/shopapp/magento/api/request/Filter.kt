package com.shopapp.magento.api.request

enum class Filter(val value: String) {
    FIELD("field"),
    VALUE("value"),
    CONDITION("condition_type")
}