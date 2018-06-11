package com.shopapp.gateway.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator") //https://youtrack.jetbrains.com/issue/KT-19300
@Parcelize
data class Shop(
    val name: String,
    val description: String?,
    val privacyPolicy: Policy?,
    val refundPolicy: Policy?,
    val termsOfService: Policy?
) : Parcelable