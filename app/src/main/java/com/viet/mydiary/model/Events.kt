package com.viet.mydiary.model

import java.util.*

class Events(
    var title: String,
    var event: String,
    var date: Date
) : Comparable<Events> {
    var id: Int = 0

    constructor(
        id: Int,
        title: String,
        event: String,
        date: Date
    ) : this(title, event, date) {
        this.id = id
    }

    override fun compareTo(event: Events): Int {
        return this.date.compareTo(event.date)
    }
}