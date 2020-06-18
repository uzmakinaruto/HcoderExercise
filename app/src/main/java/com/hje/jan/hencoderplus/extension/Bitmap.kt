package com.hje.jan.hencoderplus.extension

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

fun getBitmap(context: Context, id: Int, width: Float): Bitmap {
    val option = BitmapFactory.Options()
    option.inJustDecodeBounds = true
    BitmapFactory.decodeResource(context.resources, id, option)
    option.inJustDecodeBounds = false
    option.inSampleSize = (option.outWidth.toFloat() / width).toInt()
    return BitmapFactory.decodeResource(context.resources, id, option)
}