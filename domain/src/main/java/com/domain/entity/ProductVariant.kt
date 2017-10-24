package com.domain.entity

import android.os.Parcel
import android.os.Parcelable

data class ProductVariant(var id: String,
                          var title: String,
                          var price: String,
                          var isAvailable: Boolean,
                          var selectedOptions: List<VariantOption>,
                          var image: Image? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.createTypedArrayList(VariantOption.CREATOR),
            source.readParcelable<Image>(Image::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeString(price)
        writeInt((if (isAvailable) 1 else 0))
        writeTypedList(selectedOptions)
        writeParcelable(image, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductVariant> = object : Parcelable.Creator<ProductVariant> {
            override fun createFromParcel(source: Parcel): ProductVariant = ProductVariant(source)
            override fun newArray(size: Int): Array<ProductVariant?> = arrayOfNulls(size)
        }
    }
}
