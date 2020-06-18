package com.hje.jan.hencoderplus.extension

import android.content.res.Resources

fun Float.dp(): Float {
    return Resources.getSystem().displayMetrics.density * this
}


fun Int.dp(): Float {
    return Resources.getSystem().displayMetrics.density * this
}

