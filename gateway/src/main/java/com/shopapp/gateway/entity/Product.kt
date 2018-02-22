package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal
import java.util.*

data class Product(var id: String,
                   var title: String,
                   var productDescription: String,
                   var additionalDescription: String,
                   var currency: String,
                   var price: BigDecimal,
                   var hasAlternativePrice: Boolean,
                   var discount: String? = null,
                   var vendor: String,
                   var type: String,
                   var createdAt: Date,
                   var updatedAt: Date,
                   var tags: List<String>,
                   var images: List<Image>,
                   var options: List<ProductOption>,
                   var variants: List<ProductVariant>,
                   var paginationValue: String? = null
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readSerializable() as BigDecimal,
        1 == source.readInt(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readSerializable() as Date,
        source.readSerializable() as Date,
        source.createStringArrayList(),
        source.createTypedArrayList(Image.CREATOR),
        source.createTypedArrayList(ProductOption.CREATOR),
        source.createTypedArrayList(ProductVariant.CREATOR),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeString(productDescription)
        writeString(additionalDescription)
        writeString(currency)
        writeSerializable(price)
        writeInt((if (hasAlternativePrice) 1 else 0))
        writeString(discount)
        writeString(vendor)
        writeString(type)
        writeSerializable(createdAt)
        writeSerializable(updatedAt)
        writeStringList(tags)
        writeTypedList(images)
        writeTypedList(options)
        writeTypedList(variants)
        writeString(paginationValue)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(source: Parcel): Product = Product(source)
            override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
        }
    }
}
