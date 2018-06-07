package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class Customer(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val isAcceptsMarketing: Boolean,
    val addressList: List<Address>,
    val defaultAddress: Address?
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        1 == source.readInt(),
        source.createTypedArrayList(Address.CREATOR),
        source.readParcelable<Address>(Address::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(email)
        writeString(firstName)
        writeString(lastName)
        writeString(phone)
        writeInt((if (isAcceptsMarketing) 1 else 0))
        writeTypedList(addressList)
        writeParcelable(defaultAddress, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Customer> = object : Parcelable.Creator<Customer> {
            override fun createFromParcel(source: Parcel): Customer = Customer(source)
            override fun newArray(size: Int): Array<Customer?> = arrayOfNulls(size)
        }
    }
}