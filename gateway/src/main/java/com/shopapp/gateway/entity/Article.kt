package com.shopapp.gateway.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@SuppressLint("ParcelCreator") //https://youtrack.jetbrains.com/issue/KT-19300
@Parcelize
data class Article(
    val id: String,
    val title: String,
    val content: String,
    val contentHTML: String,
    val image: Image?,
    val author: Author,
    val tags: List<String>,
    val blogId: String,
    val blogTitle: String,
    val publishedAt: Date,
    val url: String,
    val paginationValue: String? = null
) : Parcelable

