package com.example.tyasw.myhikes

/**
 * Created by tyasw on 5/29/18.
 */
class Hike {
    var id: Int = 0
    var userId: Int = 0
    var name: String? = null
    var length: Double = 0.0
    var difficulty: String? = null

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
}