package com.shinjaehun.winternotes.common

import android.widget.Toast
import androidx.fragment.app.Fragment

internal fun Fragment.makeToast(value: String) {
    Toast.makeText(activity, value, Toast.LENGTH_SHORT).show()
}

