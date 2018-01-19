package com.domain.entity

import android.os.Parcel
import android.os.Parcelable

data class ProductVariant(var id: String,
                          var title: String,
                          var price: Float,
                          var isAvailable: Boolean,
                          var selectedOptions: List<VariantOption>,
                          var image: Image? = null,
                          var productImage: Image? = null,
                          var productId: String
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readFloat(),
        1 == source.readInt(),
        source.createTypedArrayList(VariantOption.CREATOR),
        source.readParcelable<Image>(Image::class.java.classLoader),
        source.readParcelable<Image>(Image::class.java.classLoader),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeFloat(price)
        writeInt((if (isAvailable) 1 else 0))
        writeTypedList(selectedOptions)
        writeParcelable(image, 0)
        writeParcelable(productImage, 0)
        writeString(productId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductVariant> = object : Parcelable.Creator<ProductVariant> {
            override fun createFromParcel(source: Parcel): ProductVariant = ProductVariant(source)
            override fun newArray(size: Int): Array<ProductVariant?> = arrayOfNulls(size)
        }
    }
}
