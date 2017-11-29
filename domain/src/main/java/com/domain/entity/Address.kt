package com.domain.entity

import android.os.Parcel
import android.os.Parcelable

data class Address(
        val address: String,
        val city: String,
        val country: String,
        val firstName: String,
        val lastName: String,
        val zip: String,
        val phone: String?
) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(address)
        writeString(city)
        writeString(country)
        writeString(firstName)
        writeString(lastName)
        writeString(zip)
        writeString(phone)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Address> = object : Parcelable.Creator<Address> {
            override fun createFromParcel(source: Parcel): Address = Address(source)
            override fun newArray(size: Int): Array<Address?> = arrayOfNulls(size)
        }
    }
}