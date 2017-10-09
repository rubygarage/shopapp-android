package com.shopapicore.entity

import android.os.Parcel
import android.os.Parcelable

open class CategoryDetails(var additionalDescription: String,
                           var productList: List<Product>) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.createTypedArrayList(Product.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(additionalDescription)
        writeTypedList(productList)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CategoryDetails> = object : Parcelable.Creator<CategoryDetails> {
            override fun createFromParcel(source: Parcel): CategoryDetails = CategoryDetails(source)
            override fun newArray(size: Int): Array<CategoryDetails?> = arrayOfNulls(size)
        }
    }
}
