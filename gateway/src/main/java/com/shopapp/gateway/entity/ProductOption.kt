package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProductOption(
    val id: String,
    val name: String,
    val values: List<String>
) : Parcelable