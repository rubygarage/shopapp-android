package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class Address(
    val id: String = NO_ID,
    val address: String,
    val secondAddress: String?,
    val city: String,
    val country: String,
    val state: String?,
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
        writeString(country)
        writeString(state)
        writeString(firstName)
        writeString(lastName)
        writeString(zip)
        writeString(phone)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Address

        if (address != other.address) return false
        if (secondAddress != other.secondAddress) return false
        if (city != other.city) return false
        if (country != other.country) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (zip != other.zip) return false
        if (phone != other.phone) return false

        return true
    }

    override fun hashCode(): Int {
        var result = address.hashCode()
        result = 31 * result + (secondAddress?.hashCode() ?: 0)
        result = 31 * result + city.hashCode()
        result = 31 * result + country.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + zip.hashCode()
        result = 31 * result + (phone?.hashCode() ?: 0)
        return result
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