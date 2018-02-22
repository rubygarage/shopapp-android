package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class Author(var firstName: String,
                  var lastName: String,
                  var fullName: String,
                  var email: String,
                  var bio: String?
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(firstName)
        writeString(lastName)
        writeString(fullName)
        writeString(email)
        writeString(bio)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Author> = object : Parcelable.Creator<Author> {
            override fun createFromParcel(source: Parcel): Author = Author(source)
            override fun newArray(size: Int): Array<Author?> = arrayOfNulls(size)
        }
    }
}