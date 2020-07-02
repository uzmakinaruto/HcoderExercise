package com.hje.jan.hencoderplus.customview

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.hje.jan.hencoderplus.R
import com.hje.jan.hencoderplus.extension.dp
import com.hje.jan.hencoderplus.extension.getBitmap

class MultiTouchView2 : View {
    companion object {
        const val TAG = "MultiTouchView2"
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val bitmap: Bitmap
    var offsetX = 0f
    var offsetY = 0f
    var downX = 0f
    var downY = 0f
    var originOffsetX = 0f
    var originOffsetY = 0f

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        bitmap = getBitmap(getContext(), R.drawable.chihuo, 300.dp())
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.actionMasked
        val pointerCount = event?.pointerCount!!
        var sumX = 0f
        var sumY = 0f
        var centerX = 0f
        var centerY = 0f
        for (i in 0 until pointerCount) {
            sumX += event?.getX(i)
            sumY += event?.getY(i)
        }
        centerX = sumX / pointerCount
        centerY = sumY / pointerCount
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_UP,
            MotionEvent.ACTION_POINTER_UP
            -> {
                downX = centerX
                downY = centerY
                originOffsetX = offsetX
                originOffsetY = offsetY
                Log.d(TAG, "ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = originOffsetX + centerX - downX
                offsetY = originOffsetY + centerY - downY
                invalidate()
                Log.d(TAG, "ACTION_MOVE")
            }
        }
        return true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        offsetX = (width * 1f - bitmap.width) / 2
        offsetY = (height * 1f - bitmap.height) / 2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, offsetX, offsetY, paint)
    }
}