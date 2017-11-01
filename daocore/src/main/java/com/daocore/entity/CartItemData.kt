package com.daocore.entity

import io.requery.*

@Entity
interface CartItemData : Persistable {

    @get:Key
    var id: String
    @get:ForeignKey
    @get:OneToOne
    var productVariant: ProductVariantData
    var quantity: Int
}