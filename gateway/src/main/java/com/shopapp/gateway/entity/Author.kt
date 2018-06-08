package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Author(
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val email: String,
    val bio: String?
) : Parcelable