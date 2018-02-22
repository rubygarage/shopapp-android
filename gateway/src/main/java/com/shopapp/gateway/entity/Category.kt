package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class Category(var id: String,
                    var title: String,
                    var categoryDescription: String,
                    var additionalDescription: String,
                    var image: Image?,
                    var updatedAt: Date,
                    var productList: List<Product>,
                    var paginationValue: String? = null
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readParcelable<Image>(Image::class.java.classLoader),
        source.readSerializable() as Date,
        source.createTypedArrayList(Product.CREATOR),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeString(categoryDescription)
        writeString(additionalDescription)
        writeParcelable(image, 0)
        writeSerializable(updatedAt)
        writeTypedList(productList)
        writeString(paginationValue)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = Category(source)
            override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
        }
    }
}
