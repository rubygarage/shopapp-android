package com.shopapp.data.dao.adapter

import com.shopapp.gateway.entity.VariantOption
import com.shopapp.data.dao.entity.VariantOptionData
import com.shopapp.data.dao.entity.VariantOptionDataEntity

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