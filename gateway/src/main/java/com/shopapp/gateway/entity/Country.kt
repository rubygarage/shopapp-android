package com.shopapp.gateway.entity

import android.os.Parcel
import android.os.Parcelable

data class Country(
    val id: Long,
    val code: String,
    val name: String,
    val states: List<State>?
) : Parcelable {

    constructor(source: Parcel) : this(
        source.readLong(),
        source.readString(),
        source.readString(),
        source.createTypedArrayList(State.CREATOR)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeLong(id)
        writeString(code)
        writeString(name)
        writeTypedList(states)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Country> = object : Parcelable.Creator<Country> {
            override fun createFromParcel(source: Parcel): Country = Country(source)
            override fun newArray(size: Int): Array<Country?> = arrayOfNulls(size)
        }
    }
}