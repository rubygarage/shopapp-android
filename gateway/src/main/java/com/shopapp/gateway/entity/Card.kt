package com.shopapp.gateway.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator") //https://youtrack.jetbrains.com/issue/KT-19300
@Parcelize
data class Card(
    val firstName: String,
    val lastName: String,
    val cardNumber: String,
    val expireMonth: String,
    val expireYear: String,
    val verificationCode: String
) : Parcelable