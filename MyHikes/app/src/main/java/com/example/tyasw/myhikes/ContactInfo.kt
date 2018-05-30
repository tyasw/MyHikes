package com.example.tyasw.myhikes

/**
 * Created by tyasw on 5/29/18.
 */
class ContactInfo {
    var id: Int = 0
    var name: String? = null
    var phone: Int = 0

    constructor(id: Int, name: String, phone: Int) {
        this.id = id
        this.name = name
        this.phone = phone
    }
}