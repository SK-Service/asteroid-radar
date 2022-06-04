package com.udacity.asteroidradar.util

import java.text.SimpleDateFormat
import java.util.*

fun getTodayDate(): String {
    val calendar = Calendar.getInstance()
    val timeNow = calendar.time
    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formattedDate.format(timeNow)
}

fun getEndOfDateRange() : String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 7)
    val timeNow = calendar.time
    val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return formattedDate.format(timeNow)
}