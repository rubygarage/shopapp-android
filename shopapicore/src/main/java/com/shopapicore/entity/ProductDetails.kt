package com.shopapicore.entity

import android.os.Parcel
import android.os.Parcelable

class ProductDetails(var variants: List<ProductVariant>,
                     var additionalDescription: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.createTypedArrayList(ProductVariant.CREATOR),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeTypedList(variants)
        writeString(additionalDescription)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductDetails> = object : Parcelable.Creator<ProductDetails> {
            override fun createFromParcel(source: Parcel): ProductDetails = ProductDetails(source)
            override fun newArray(size: Int): Array<ProductDetails?> = arrayOfNulls(size)
        }
    }
}
