package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class Policy(var title: String,
                  var body: String,
                  var url: String
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(title)
        writeString(body)
        writeString(url)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Policy> = object : Parcelable.Creator<Policy> {
            override fun createFromParcel(source: Parcel): Policy = Policy(source)
            override fun newArray(size: Int): Array<Policy?> = arrayOfNulls(size)
        }
    }
}
