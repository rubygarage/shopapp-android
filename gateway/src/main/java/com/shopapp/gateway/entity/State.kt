package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class State(
    val id: Long,
    val countryId: Long,
    val code: String,
    val name: String
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readLong(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeLong(countryId)
        writeString(code)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<State> = object : Parcelable.Creator<State> {
            override fun createFromParcel(source: Parcel): State = State(source)
            override fun newArray(size: Int): Array<State?> = arrayOfNulls(size)
        }
    }
}