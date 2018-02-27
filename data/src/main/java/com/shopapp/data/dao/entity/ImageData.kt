package com.shopapp.data.dao.entity

import io.requery.Entity
import io.requery.Key
import io.requery.OneToOne
import io.requery.Persistable

@Entity
interface ImageData : Persistable {

    @get:Key
    var id: String
    var src: String
    var alt: String

    @get:OneToOne
    var productVariant: ProductVariantData
}