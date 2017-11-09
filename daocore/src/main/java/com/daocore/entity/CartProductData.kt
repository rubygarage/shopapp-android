package com.daocore.entity

import io.requery.*

@Entity
interface CartProductData : Persistable {

    @get:Key
    var id: String
    @get:ForeignKey
    @get:OneToOne
    var productVariant: ProductVariantData
    var productId: String
    var currency: String
    var quantity: Int
}