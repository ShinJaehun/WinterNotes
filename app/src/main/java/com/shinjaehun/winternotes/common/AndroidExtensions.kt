package com.shinjaehun.winternotes.common

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

internal fun Activity.makeToast(value: String) {
    Toast.makeText(applicationContext, value, Toast.LENGTH_SHORT).show()
}

