package com.example.tab_project

import android.os.Parcel
import android.os.Parcelable

const val SHORT = 0
const val FULL = 1
const val SWIPED = 2

class DiaryData(
    var date: String? = null,
    var title: String? = null,
    var content: String? = null,
    var icon: Int? = null,
    var isFavorite: Boolean = false,
    var viewMode: Int = SHORT
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(date)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeValue(icon)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeInt(viewMode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<DiaryData> {
        override fun createFromParcel(parcel: Parcel): DiaryData {
            return DiaryData(parcel)
        }

        override fun newArray(size: Int): Array<DiaryData?> {
            return arrayOfNulls(size)
        }
    }
}