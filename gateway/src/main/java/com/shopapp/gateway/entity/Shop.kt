package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class Shop(
    val name: String,
    val description: String?,
    val privacyPolicy: Policy?,
    val refundPolicy: Policy?,
    val termsOfService: Policy?
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readParcelable<Policy>(Policy::class.java.classLoader),
        source.readParcelable<Policy>(Policy::class.java.classLoader),
        source.readParcelable<Policy>(Policy::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(name)
        writeString(description)
        writeParcelable(privacyPolicy, 0)
        writeParcelable(refundPolicy, 0)
        writeParcelable(termsOfService, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Shop> = object : Parcelable.Creator<Shop> {
            override fun createFromParcel(source: Parcel): Shop = Shop(source)
            override fun newArray(size: Int): Array<Shop?> = arrayOfNulls(size)
        }
    }
}
