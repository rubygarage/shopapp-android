package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.util.*

@Parcelize
data class Product(
    val id: String,
    val title: String,
    val productDescription: String,
    val additionalDescription: String,
    val currency: String,
    val price: BigDecimal,
    val hasAlternativePrice: Boolean,
    val discount: String? = null,
    val vendor: String,
    val type: String,
    val createdAt: Date,
    val updatedAt: Date,
    val tags: List<String>,
    val images: List<Image>,
    val options: List<ProductOption>,
    val variants: List<ProductVariant>,
    val paginationValue: String? = null
) : Parcelable
