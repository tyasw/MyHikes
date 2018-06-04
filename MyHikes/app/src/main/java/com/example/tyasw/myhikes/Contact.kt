package com.example.tyasw.myhikes

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by tyasw on 5/29/18.
 */
class Contact : Parcelable{
    var id: Int = 0
    var hikeId: Int = 0
    var name: String? = null
    var phone: String? = null

    constructor(parcel: Parcel) {
        this.id = parcel.readInt()
        this.hikeId = parcel.readInt()
        this.name = parcel.readString()
        this.phone = parcel.readString()
    }

    // Can be written in any order
    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeInt(hikeId)
        dest.writeString(name)
        dest.writeString(phone)
    }

    override fun describeContents(): Int {
        return 0
    }

    constructor(contactId: Int, hikeId: Int, name: String, phone: String) {
        this.id = contactId
        this.hikeId = hikeId
        this.name = name
        this.phone = phone
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}