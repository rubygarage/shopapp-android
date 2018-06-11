package com.shopapp.gateway.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator") //https://youtrack.jetbrains.com/issue/KT-19300
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