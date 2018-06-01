package com.example.tyasw.myhikes

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by tyasw on 5/29/18.
 */
class Supply : Parcelable {
    var id: Int = 0
    var hikeId: Int = 0
    var name: String? = null
    var quantity: Double = 0.0

    constructor(parcel: Parcel) {
        this.id = parcel.readInt()
        this.hikeId = parcel.readInt()
        this.name = parcel.readString()
        this.quantity = parcel.readDouble()
    }

    // Can be written in any order
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(hikeId)
        dest.writeString(name)
        dest.writeDouble(quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(id: Int, hikeId: Int, name: String, quantity: Double) {
        this.id = id
        this.hikeId = hikeId
        this.name = name
        this.quantity = quantity
    }

    constructor(hikeId: Int, name: String, quantity: Double) {
        this.hikeId = hikeId
        this.name = name
        this.quantity = quantity
    }

    constructor(name: String, quantity: Double) {
        this.name = name
        this.quantity = quantity
    }

    companion object CREATOR : Parcelable.Creator<Supply> {
        override fun createFromParcel(parcel: Parcel): Supply {
            return Supply(parcel)
        }

        override fun newArray(size: Int): Array<Supply?> {
            return arrayOfNulls(size)
        }
    }
}