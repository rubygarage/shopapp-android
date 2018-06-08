package com.shopapp.gateway.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class ProductVariant(
    val id: String,
    val title: String,
    val price: BigDecimal,
    val isAvailable: Boolean,
    val selectedOptions: List<VariantOption>,
    val image: Image? = null,
    val productImage: Image? = null,
    val productId: String
) : Parcelable
