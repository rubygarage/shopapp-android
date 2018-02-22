package com.shopapp.data.dao.entity

import io.requery.*

@Entity
interface ProductVariantData : Persistable {
    @get:Key
    var id: String
    var title: String
    var price: Float
    var isAvailable: Boolean
    @get:OneToMany(mappedBy = "productVariant", cascade = [CascadeAction.DELETE, CascadeAction.SAVE])
    var selectedOptions: List<VariantOptionData>
    @get:ForeignKey
    @get:OneToOne
    var image: ImageData?
    @get:ForeignKey
    @get:OneToOne
    var productImage: ImageData?
    var productItemId: String
}