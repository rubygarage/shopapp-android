package com.shopapicore.entity

import android.os.Parcel
import android.os.Parcelable

open class VariantOption(var name: String, var value: String) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(value)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VariantOption

        if (name != other.name) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<VariantOption> = object : Parcelable.Creator<VariantOption> {
            override fun createFromParcel(source: Parcel): VariantOption = VariantOption(source)
            override fun newArray(size: Int): Array<VariantOption?> = arrayOfNulls(size)
        }
    }


}