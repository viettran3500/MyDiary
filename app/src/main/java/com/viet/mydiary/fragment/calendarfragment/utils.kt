package com.viet.mydiary.fragment.calendarfragment

import java.util.*

var dayClick: Date? = null
var lastDayClick: Date? = null
var numClick = 0
const val startFragment = 500
const val maxFragment = 1000
var index = 0

fun checkMonth(calendar: Calendar, month: Int) {
    while (calendar.get(Calendar.MONTH) != month) {
        if (calendar.get(Calendar.MONTH) > month)
            calendar.add(Calendar.MONTH, -1)
        else
            calendar.add(Calendar.MONTH, 1)
    }
}