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

class MultiTouchView1 : View {
    companion object {
        const val TAG = "MultiTouchView1"
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap: Bitmap
    private var offsetX = 0f
    private var offsetY = 0f
    private var downX = 0f
    private var downY = 0f
    private var originOffsetX = 0f
    private var originOffsetY = 0f
    private var currentPointerId = 0

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        bitmap = getBitmap(getContext(), R.drawable.chihuo, 300.dp())

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

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.actionMasked
        var index = 0
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                currentPointerId = event.getPointerId(0)
                downX = event.x
                downY = event.y
                originOffsetX = offsetX
                originOffsetY = offsetY
                Log.d(
                    TAG,
                    "ACTION_DOWN - index:${index} id:${currentPointerId} downX:${downX} downY${downY}"
                )
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                index = event.actionIndex
                downX = event.getX(index)
                downY = event.getY(index)
                originOffsetX = offsetX
                originOffsetY = offsetY
                currentPointerId = event.getPointerId(index)
                Log.d(
                    TAG,
                    "ACTION_POINTER_DOWN- index:${index} id:${currentPointerId} downX${downX} downY${downY}"
                )
            }
            MotionEvent.ACTION_MOVE -> {
                index = event.findPointerIndex(currentPointerId)
                offsetX = originOffsetX + event.getX(index) - downX
                offsetY = originOffsetY + event.getY(index) - downY
                invalidate()
                Log.d(TAG, "ACTION_MOVE- index:${index} offsetX${offsetX} offsetY${offsetY}")
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP")
            }
            MotionEvent.ACTION_POINTER_UP -> {
                index = event.actionIndex
                if (event.getPointerId(index) == currentPointerId) {
                    var newIndex = 0
                    if (index == 0) newIndex = 1
                    downX = event.getX(newIndex)
                    downY = event.getY(newIndex)
                    originOffsetX = offsetX
                    originOffsetY = offsetY
                    currentPointerId = event.getPointerId(newIndex)
                }
                Log.d(
                    TAG,
                    "ACTION_POINTER_UP- index:${event.actionIndex} id:${event.getPointerId(event.actionIndex)}"
                )
            }
        }
        return true
    }
}