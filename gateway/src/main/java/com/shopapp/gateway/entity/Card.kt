package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    val firstName: String,
    val lastName: String,
    val cardNumber: String,
    val expireMonth: String,
    val expireYear: String,
    val verificationCode: String
) : Parcelable