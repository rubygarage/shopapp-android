package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Shop(
    val name: String,
    val description: String?,
    val privacyPolicy: Policy?,
    val refundPolicy: Policy?,
    val termsOfService: Policy?
) : Parcelable