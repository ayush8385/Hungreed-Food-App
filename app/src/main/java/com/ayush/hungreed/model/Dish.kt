package com.ayush.hungreed.model

import android.os.Parcel
import android.os.Parcelable

data class Dish(
    val foodId:String?,
    val foodName: String?,
    val foodPrice:String?,
    val restaurantId:String?,
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(foodId)
        parcel.writeString(foodName)
        parcel.writeString(foodPrice)
        parcel.writeString(restaurantId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Dish> {
        override fun createFromParcel(parcel: Parcel): Dish {
            return Dish(parcel)
        }

        override fun newArray(size: Int): Array<Dish?> {
            return arrayOfNulls(size)
        }
    }

}
