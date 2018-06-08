package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VariantOption(val name: String, val value: String) : Parcelable