package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class State(
    val id: String,
    val countryId: String,
    val code: String,
    val name: String
) : Parcelable