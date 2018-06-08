package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    val id: String,
    val code: String,
    val name: String,
    val states: List<State>?
) : Parcelable