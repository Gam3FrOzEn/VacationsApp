package com.android.vacationsapp.models

import java.io.Serializable

class Vacation : Serializable {
    var id = 0
    var name: String? = null
    var hotel: String? = null
    var location: String? = null
    var money: String? = null
    var description: String? = null
    var image: String? = null

    constructor() {}
    constructor(
        name: String?,
        hotel: String?,
        location: String?,
        money: String?,
        description: String?,
        image: String?
    ) {
        this.name = name
        this.hotel = hotel
        this.location = location
        this.money = money
        this.description = description
        this.image = image
    }

    constructor(
        id: Int,
        name: String?,
        hotel: String?,
        location: String?,
        money: String?,
        description: String?,
        image: String?
    ) {
        this.id = id
        this.name = name
        this.hotel = hotel
        this.location = location
        this.money = money
        this.description = description
        this.image = image
    }
}