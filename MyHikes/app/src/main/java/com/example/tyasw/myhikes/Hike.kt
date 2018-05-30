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

    // Read in the same sequence as they were written in the parcel
    private constructor(parcel: Parcel) {
        if (parcel.dataSize() == 5) {

        } else if (parcel.dataSize() == 4) {

        } else {
            // Size is 1
        }


        this.id = parcel.readInt()
        this.userId = parcel.readInt()
        this.name = parcel.readString()
        this.length = parcel.readDouble()
        this.difficulty = parcel.readString()
    }

    // Can be written in any order
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(userId)
        dest.writeString(name)
        dest.writeDouble(length)
        dest.writeString(difficulty)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(id: Int, userId: Int, name: String, length: Double, difficulty: String) {
        this.id = id
        this.userId = userId
        this.name = name
        this.length = length
        this.difficulty = difficulty
    }

    constructor(userId: Int, name: String, length: Double, difficulty: String) {
        this.userId = userId
        this.name = name
        this.length = length
        this.difficulty = difficulty
    }

    constructor(userId: Int) {
        this.userId = userId
        this.name = ""
        this.difficulty = ""
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