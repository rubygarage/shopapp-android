package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class VariantOption(var name: String, var value: String) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(value)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VariantOption> = object : Parcelable.Creator<VariantOption> {
            override fun createFromParcel(source: Parcel): VariantOption = VariantOption(source)
            override fun newArray(size: Int): Array<VariantOption?> = arrayOfNulls(size)
        }
    }
}