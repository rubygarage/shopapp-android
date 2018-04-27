package com.shopapp.magento.api.request

enum class ConditionType(val value: String) {
    DEFAULT("eq"),
    SEARCH_CONDITION("like"),
    NOT_EQUAL("neq")
}