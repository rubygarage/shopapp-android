package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class Card(
    val firstName: String,
    val lastName: String,
    val cardNumber: String,
    val expireMonth: String,
    val expireYear: String,
    val verificationCode: String
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readString(),
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
        writeString(cardNumber)
        writeString(expireMonth)
        writeString(expireYear)
        writeString(verificationCode)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Card> = object : Parcelable.Creator<Card> {
            override fun createFromParcel(source: Parcel): Card = Card(source)
            override fun newArray(size: Int): Array<Card?> = arrayOfNulls(size)
        }
    }
}