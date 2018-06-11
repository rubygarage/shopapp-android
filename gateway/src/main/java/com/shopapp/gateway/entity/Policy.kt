package com.shopapp.gateway.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator") //https://youtrack.jetbrains.com/issue/KT-19300
@Parcelize
data class Policy(
    val title: String,
    val body: String,
    val url: String
) : Parcelable
