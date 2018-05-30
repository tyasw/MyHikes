package com.example.tyasw.myhikes

/**
 * Created by tyasw on 5/29/18.
 */
class Account {
    var id: Int = 0
    var username: String? = null
    var encryptedPassword: String? = null

    constructor(id: Int, username: String, encryptedPassword: String) {
        this.id = id
        this.username = username
        this.encryptedPassword = encryptedPassword
    }
}