package com.data.dao.entity

import com.client.shop.getaway.entity.ProductVariant
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
    var product: ProductVariant
}