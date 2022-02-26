package com.android.vacationsapp.models

class Notification {
    var id = 0
    var name: String? = null
    var desc: String? = null
    var date: String? = null
    var time: String? = null
    var vacation: String? = null

    constructor() {}
    constructor(name: String?, desc: String?, date: String?, time: String?, vacation: String?) {
        this.name = name
        this.desc = desc
        this.date = date
        this.time = time
        this.vacation = vacation
    }

    constructor(
        id: Int,
        name: String?,
        desc: String?,
        date: String?,
        time: String?,
        vacation: String?
    ) {
        this.id = id
        this.name = name
        this.desc = desc
        this.date = date
        this.time = time
        this.vacation = vacation
    }
}