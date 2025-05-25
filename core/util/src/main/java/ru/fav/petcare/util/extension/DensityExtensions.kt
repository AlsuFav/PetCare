package ru.fav.petcare.util.extension

import android.content.res.Resources

val Float.dp: Int
    get() = (this * Resources.getSystem().displayMetrics.density).toInt()
