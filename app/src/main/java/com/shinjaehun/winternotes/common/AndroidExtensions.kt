package com.shinjaehun.winternotes.common

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

internal fun Fragment.makeToast(value: String) {
    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show()
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

