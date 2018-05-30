package com.example.tyasw.myhikes

/**
 * Created by tyasw on 5/29/18.
 */
class Supply {
    var id: Int = 0
    var supplyId: Int = 0
    var hikeId: Int = 0
    var quantity: Double = 0.0

    constructor(id: Int, supplyId: Int, hikeId: Int, quantity: Double) {
        this.id = id
        this.supplyId = supplyId
        this.hikeId = hikeId
        this.quantity = quantity
    }

    constructor(supplyId: Int, hikeId: Int, quantity: Double) {
        this.supplyId = supplyId
        this.hikeId = hikeId
        this.quantity = quantity
    }
}