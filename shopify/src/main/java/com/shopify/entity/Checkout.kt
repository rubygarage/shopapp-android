package com.shopify.entity

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal

data class Checkout(
        val checkoutId: String,
        val webUrl: String,
        val requiresShipping: Boolean,
        val subtotalPrice: BigDecimal,
        val totalPrice: BigDecimal,
        val taxPrice: BigDecimal,
        val currency: String
) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            1 == source.readInt(),
            source.readSerializable() as BigDecimal,
            source.readSerializable() as BigDecimal,
            source.readSerializable() as BigDecimal,
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(checkoutId)
        writeString(webUrl)
        writeInt((if (requiresShipping) 1 else 0))
        writeSerializable(subtotalPrice)
        writeSerializable(totalPrice)
        writeSerializable(taxPrice)
        writeString(currency)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Checkout> = object : Parcelable.Creator<Checkout> {
            override fun createFromParcel(source: Parcel): Checkout = Checkout(source)
            override fun newArray(size: Int): Array<Checkout?> = arrayOfNulls(size)
        }
    }
}