package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Policy(
    val title: String,
    val body: String,
    val url: String
) : Parcelable
