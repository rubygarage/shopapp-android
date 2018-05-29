package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal
import java.util.*

data class Product(
    val id: String,
    val title: String,
    val productDescription: String,
    val additionalDescription: String,
    val currency: String,
    val price: BigDecimal,
    val hasAlternativePrice: Boolean,
    val discount: String? = null,
    val vendor: String,
    val type: String,
    val createdAt: Date,
    val updatedAt: Date,
    val tags: List<String>,
    val images: List<Image>,
    val options: List<ProductOption>,
    val variants: List<ProductVariant>,
    val paginationValue: String? = null
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
