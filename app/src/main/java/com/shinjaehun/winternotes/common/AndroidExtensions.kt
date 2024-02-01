package com.shinjaehun.winternotes.common

import android.app.Activity
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

internal fun Activity.makeToast(value: String) {
    Toast.makeText(applicationContext, value, Toast.LENGTH_SHORT).show()
}

internal fun currentTime() = SimpleDateFormat(
    "yyyy MMMM dd, EEEE, HH:mm a",
    Locale.getDefault()).format(Date())


internal fun simpleDate(dateString: String) : String {
    val toDate = SimpleDateFormat("yyyy MMMM dd, EEEE, HH:mm a").parse(dateString)
    return SimpleDateFormat(
        "yyyy MMMM dd",
        Locale.getDefault()).format(toDate)
}

