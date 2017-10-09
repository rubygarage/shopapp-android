package com.shopapicore.entity

import android.os.Parcel
import android.os.Parcelable
import java.util.*

open class Product(var id: String,
                   var title: String,
                   var productDescription: String,
                   var currency: String,
                   var price: String,
                   var discount: String? = null,
                   var vendor: String,
                   var type: String,
                   var createdAt: Date,
                   var updatedAt: Date,
                   var tags: List<String>,
                   var images: List<Image>,
                   var productDetails: ProductDetails,

                   var paginationValue: String? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date,
            source.readSerializable() as Date,
            source.createStringArrayList(),
            source.createTypedArrayList(Image.CREATOR),
            source.readParcelable<ProductDetails>(ProductDetails::class.java.classLoader),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeString(productDescription)
        writeString(currency)
        writeString(price)
        writeString(discount)
        writeString(vendor)
        writeString(type)
        writeSerializable(createdAt)
        writeSerializable(updatedAt)
        writeStringList(tags)
        writeTypedList(images)
        writeParcelable(productDetails, 0)
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
