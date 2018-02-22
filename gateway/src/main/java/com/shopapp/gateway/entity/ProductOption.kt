package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class ProductOption(var id: String,
                         var name: String,
                         var values: List<String>
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(name)
        writeStringList(values)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ProductOption> = object : Parcelable.Creator<ProductOption> {
            override fun createFromParcel(source: Parcel): ProductOption = ProductOption(source)
            override fun newArray(size: Int): Array<ProductOption?> = arrayOfNulls(size)
        }
    }
}