package com.shopapp.gateway.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator") //https://youtrack.jetbrains.com/issue/KT-19300
@Parcelize
data class ProductOption(
    val id: String,
    val name: String,
    val values: List<String>
) : Parcelable