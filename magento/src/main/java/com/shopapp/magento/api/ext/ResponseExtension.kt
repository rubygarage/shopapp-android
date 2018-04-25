package com.shopapp.magento.api.ext

import com.shopapp.magento.api.response.CustomAttribute

fun List<CustomAttribute>.getValue(attribute: String): String? {
    return find { it.attributeCode == attribute }?.value?.data
}