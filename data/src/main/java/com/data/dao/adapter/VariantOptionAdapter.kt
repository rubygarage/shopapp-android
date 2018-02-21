package com.data.dao.adapter

import com.data.dao.entity.VariantOptionData
import com.data.dao.entity.VariantOptionDataEntity
import com.client.shop.gateway.entity.VariantOption

object VariantOptionAdapter {

    fun adaptToStore(adaptee: VariantOption): VariantOptionData {
        val product = VariantOptionDataEntity()
        product.optionName = adaptee.name
        product.optionValue = adaptee.value
        return product
    }

    fun adaptFromStore(adaptee: VariantOptionData): VariantOption {
        return VariantOption(adaptee.optionName, adaptee.optionValue)
    }
}