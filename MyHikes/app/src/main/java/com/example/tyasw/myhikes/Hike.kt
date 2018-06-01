package com.example.tyasw.myhikes

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by tyasw on 5/29/18.
 */
class Hike : Parcelable {
    var id: Int = 0
    var userId: Int = 0
    var name: String? = null
    var length: Double = 0.0
    var difficulty: String? = null
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    // Read in the same sequence as they were written in the parcel
    private constructor(parcel: Parcel) {
        this.id = parcel.readInt()
        this.userId = parcel.readInt()
        this.name = parcel.readString()
        this.length = parcel.readDouble()
        this.difficulty = parcel.readString()
        this.latitude = parcel.readDouble()
        this.longitude = parcel.readDouble()
    }

    // Can be written in any order
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(userId)
        dest.writeString(name)
        dest.writeDouble(length)
        dest.writeString(difficulty)
        dest.writeDouble(latitude)
        dest.writeDouble(longitude)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(id: Int, userId: Int, name: String, length: Double, difficulty: String,
                latitude: Double, longitude: Double) {
        this.id = id
        this.userId = userId
        this.name = name
        this.length = length
        this.difficulty = difficulty
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor(userId: Int, name: String, length: Double, difficulty: String,
                latitude: Double, longitude: Double) {
        this.userId = userId
        this.name = name
        this.length = length
        this.difficulty = difficulty
        this.latitude = latitude
        this.longitude = longitude
    }

    constructor(userId: Int) {
        this.userId = userId
        this.name = ""
        this.length = 0.0
        this.difficulty = ""
        this.latitude = 0.0
        this.longitude = 0.0
    }

    companion object CREATOR : Parcelable.Creator<Hike> {
        override fun createFromParcel(parcel: Parcel): Hike {
            return Hike(parcel)
        }

        override fun newArray(size: Int): Array<Hike?> {
            return arrayOfNulls(size)
        }
    }
}