package com.viet.mydiary.utils

import java.text.SimpleDateFormat
import java.util.*

var dayClick: Date? = null
var lastDayClick: Date? = null
const val startFragment = 500
const val maxFragment = 1000
var index = 0

fun textToDate(text: String): Date {
    return SimpleDateFormat("dd/MM/yyyy hh:mm").parse(text)
}

fun coverDate(date: Date): String {
    return SimpleDateFormat("dd/MM/yyyy hh:mm").format(date)
}

fun coverD(date: Date): String {
    return SimpleDateFormat("dd/MM/yyyy").format(date)
}