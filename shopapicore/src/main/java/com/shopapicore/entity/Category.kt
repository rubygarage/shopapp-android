package com.shopapicore.entity

import android.os.Parcel
import android.os.Parcelable
import java.util.*

open class Category(var id: String,
                    var title: String,
                    var categoryDescription: String,
                    var image: Image?,
                    var updatedAt: Date,
                    var categoryDetails: CategoryDetails,
                    var paginationValue: Any? = null) : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readParcelable<Image>(Image::class.java.classLoader),
            source.readSerializable() as Date,
            source.readParcelable<CategoryDetails>(CategoryDetails::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(id)
        writeString(title)
        writeString(categoryDescription)
        writeParcelable(image, 0)
        writeSerializable(updatedAt)
        writeParcelable(categoryDetails, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Category> = object : Parcelable.Creator<Category> {
            override fun createFromParcel(source: Parcel): Category = Category(source)
            override fun newArray(size: Int): Array<Category?> = arrayOfNulls(size)
        }
    }
}
