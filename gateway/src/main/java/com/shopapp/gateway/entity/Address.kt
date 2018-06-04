package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class Address(
    val id: String = NO_ID,
    val address: String,
    val secondAddress: String?,
    val city: String,
    val country: Country,
    val state: State?,
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
        source.readParcelable<Country>(Country::class.java.classLoader),
        source.readParcelable<State>(State::class.java.classLoader),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(address)
        writeString(secondAddress)
        writeString(city)
        writeParcelable(country, 0)
        writeParcelable(state, 0)
        writeString(firstName)
        writeString(lastName)
        writeString(zip)
        writeString(phone)
    }

    companion object {

        const val NO_ID = "no_id"

        @JvmField
        val CREATOR: Parcelable.Creator<Address> = object : Parcelable.Creator<Address> {
            override fun createFromParcel(source: Parcel): Address = Address(source)
            override fun newArray(size: Int): Array<Address?> = arrayOfNulls(size)
        }
    }
}