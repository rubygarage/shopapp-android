package com.data.dao.entity

import io.requery.*

@Entity
interface ProductVariantData : Persistable {

    @get:Key
    var id: String
    var title: String
    var price: Float
    var isAvailable: Boolean
    @get:ForeignKey
    @get:OneToOne
    var image: ImageData?
}