package com.example.tyasw.myhikes

/**
 * Created by tyasw on 5/29/18.
 */
class SupplyType {
    var id: Int = 0
    var name: String? = null

    constructor(id: Int, name: String) {
        this.id = id
        this.name = name
    }

    constructor(name: String) {
        this.name = name
    }
}