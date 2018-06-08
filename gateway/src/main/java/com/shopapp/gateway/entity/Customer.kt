package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Customer(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val phone: String?,
    val isAcceptsMarketing: Boolean,
    val addressList: List<Address>,
    val defaultAddress: Address?
) : Parcelable