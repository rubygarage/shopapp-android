package com.shopapp.data.dao.entity

import io.requery.Entity
import io.requery.Generated
import io.requery.Key
import io.requery.ManyToOne

@Entity
interface VariantOptionData {
    @get:Key
    @get:Generated
    var id: Int
    var optionName: String
    var optionValue: String

    @get:ManyToOne
    var productVariant: ProductVariantData
}