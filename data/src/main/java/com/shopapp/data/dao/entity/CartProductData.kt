package com.shopapp.data.dao.entity

import io.requery.*

@Entity
interface CartProductData : Persistable {

    @get:Key
    var id: String
    @get:ForeignKey
    @get:OneToOne
    var productVariant: ProductVariantData
    var title: String
    var currency: String
    var quantity: Int
}