package com.shopapp.gateway.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator") //https://youtrack.jetbrains.com/issue/KT-19300
@Parcelize
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

    companion object {

        const val NO_ID = "no_id"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Address

        if (address != other.address) return false
        if (secondAddress != other.secondAddress) return false
        if (city != other.city) return false
        if (country != other.country) return false
        if (state != other.state) return false
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
        result = 31 * result + (state?.hashCode() ?: 0)
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + zip.hashCode()
        result = 31 * result + (phone?.hashCode() ?: 0)
        return result
    }
}