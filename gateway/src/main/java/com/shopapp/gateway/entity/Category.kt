package com.shopapp.gateway.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@SuppressLint("ParcelCreator") //https://youtrack.jetbrains.com/issue/KT-19300
@Parcelize
data class Category(
    val id: String,
    val title: String,
    val categoryDescription: String,
    val additionalDescription: String,
    val image: Image?,
    val updatedAt: Date,
    val childrenCategoryList: List<Category>,
    val productList: List<Product>,
    val paginationValue: String? = null
) : Parcelable